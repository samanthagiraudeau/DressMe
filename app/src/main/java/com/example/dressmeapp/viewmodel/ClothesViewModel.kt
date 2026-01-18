
package com.example.dressmeapp.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.dressmeapp.data.AppDatabase
import com.example.dressmeapp.data.ClothesRepository
import com.example.dressmeapp.enums.CategoryEnum
import com.example.dressmeapp.enums.RulesEnum
import com.example.dressmeapp.enums.SubCategoryEnum
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.ui.screens.OutfitScreen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

data class Outfit(val manteau: Clothes?, val haut: Clothes?, val bas: Clothes?, val chaussures: Clothes?, val global: Clothes?, val teeShirt: Clothes?)

class ClothesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ClothesRepository

    val allClothes by lazy { repo.allClothes }

    init {
        val db = AppDatabase.getInstance(application)
        repo = ClothesRepository(db.clothesDao())
    }

    fun addClothes(nom: String?, category: String, subCategory: String?, color: String, season: String, imageUri: String, motif: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.insert(Clothes(category = category.lowercase(), subCategory = subCategory?.lowercase(), color = color, season = season, imageUri = imageUri, motif = motif, nom = nom))
        }
    }


    fun deleteClothes(item: Clothes) {
        viewModelScope.launch(Dispatchers.IO) {
            // Supprimer le fichier local
            deleteLocalImageIfExists(item.imageUri)
            // Supprimer l'item de la base
            repo.delete(item)
        }
    }

    private fun deleteLocalImageIfExists(path: String) {
        try {
            val file = File(path)
            if (file.exists()) file.delete()
        } catch (_: Throwable) { /* ignore */
        }
    }


    suspend fun getRandomOutfit(toggleGlobal: Boolean, toggleGiletWithTeeShirt: Boolean, allRules: List<Rule>): Outfit? = withContext(Dispatchers.IO) {
        val manteaux = repo.getByType(CategoryEnum.MANTEAU.label.lowercase())
        val pullsAndGilets = repo.getPullEtGilet()
        val teeShirts = repo.getTeeShirts()
        val allBas = repo.getByType(CategoryEnum.BAS.label.lowercase())
        val shoes = repo.getByType(CategoryEnum.CHAUSSURES.label.lowercase())
        val global = repo.getByType(CategoryEnum.GLOBAL.label.lowercase())



        val colorRules = allRules.filter { rule ->  rule.type == RulesEnum.COLORS.label }
        val clotheRules = allRules.filter { rule ->  rule.type == RulesEnum.CLOTHES.label }
        var isOutfitValid = false
        var outfit : Outfit? = null


        while(!isOutfitValid) {
            outfit = generateRandomOutfit(manteaux, global, pullsAndGilets, teeShirts, allBas, shoes, toggleGlobal, toggleGiletWithTeeShirt)
            isOutfitValid = isOutfitOk(outfit, colorRules, clotheRules)
            Log.d("tag", isOutfitValid.toString())
        }

        outfit


    }

    fun isOutfitOk(outfit: Outfit, colorRules: List<Rule>, clotheRules: List<Rule>): Boolean {
        val outfitColors = listOfNotNull(
            outfit.global?.color,
            outfit.bas?.color,
            outfit.haut?.color,
            outfit.chaussures?.color,
            outfit.manteau?.color
        )

        // Règles des couleurs
        colorRules.forEach { rule ->
            val hasColor1 = outfitColors.contains(rule.first)
            val hasColor2 = outfitColors.contains(rule.second)

            if (hasColor1 && hasColor2) return false
        }

        // Règles des vêtements
        val outfitId = listOfNotNull(
            outfit.global?.id.toString(),
            outfit.bas?.id.toString(),
            outfit.haut?.id.toString(),
            outfit.chaussures?.id.toString(),
            outfit.manteau?.id.toString()
        )

        clotheRules.forEach { rule ->
            val hasClothe1 = outfitId.contains(rule.first)
            val hasClothe2 = outfitId.contains(rule.second)

            if (hasClothe1 && hasClothe2) return false
        }

        // Pas de règle violée -> outfit ok
        return true
    }

    fun generateRandomOutfit(manteaux: List<Clothes>, global: List<Clothes>, pullsAndGilets: List<Clothes>, teeShirts: List<Clothes>, allBas: List<Clothes>, chaussures: List<Clothes>, toggleGlobal: Boolean, toggleGiletWithTeeShirt: Boolean): Outfit {
        var robe: Clothes? = null
        var haut: Clothes? = null
        var bas: Clothes? = null
        var teeShirt: Clothes? = null
        var manteau: Clothes? = null
        var shoe: Clothes? = null

        if(toggleGlobal) {
            robe = global.randomOrNull()
        } else {
            bas = allBas.randomOrNull()
            haut = pullsAndGilets.randomOrNull()
            if(toggleGiletWithTeeShirt && haut?.subCategory == SubCategoryEnum.GILET.label.lowercase()) {
                teeShirt = teeShirts.randomOrNull()
            }
        }
        shoe = chaussures.randomOrNull()
        manteau = manteaux.randomOrNull()

        return Outfit (
            global = robe,
            manteau = manteau,
            haut = haut,
            teeShirt = teeShirt,
            bas = bas,
            chaussures = shoe
        )

    }
}
