
package com.example.dressmeapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.Uri
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
import com.example.dressmeapp.utils.copyImageToInternalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

data class Outfit(val manteau: Clothes?, val haut: Clothes?, val bas: Clothes?, val chaussures: Clothes?, val global: Clothes?, val teeShirt: Clothes?)

class ClothesViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: ClothesRepository
    private val _outfit = MutableStateFlow<Outfit?>(null)
    val outfit = _outfit.asStateFlow()
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


    suspend fun getRandomOutfit(toggleGlobal: Boolean, toggleGiletWithTeeShirt: Boolean, allRules: List<Rule>, season: String): Outfit? = withContext(Dispatchers.IO) {
        val manteaux = repo.getByTypeAndSeason(CategoryEnum.MANTEAU.label.lowercase(), season)
        val pullsAndGilets = repo.getPullEtGilet(season)
        val teeShirts = repo.getTeeShirts(season)
        val allBas = repo.getByTypeAndSeason(CategoryEnum.BAS.label.lowercase(), season)
        val shoes = repo.getByTypeAndSeason(CategoryEnum.CHAUSSURES.label.lowercase(), season)
        val global = repo.getByTypeAndSeason(CategoryEnum.GLOBAL.label.lowercase(), season)

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


    fun isOutfitOk(
        outfit: Outfit,
        colorRules: List<Rule>,
        clotheRules: List<Rule>
    ): Boolean {

        val outfitColors = listOfNotNull(
            outfit.global?.color?.lowercase(),
            outfit.bas?.color?.lowercase(),
            outfit.haut?.color?.lowercase(),
            outfit.chaussures?.color?.lowercase(),
            outfit.manteau?.color?.lowercase()
        )

        // ------------------------------------------------------
        // 1️⃣ RÈGLES DE COULEUR
        // ------------------------------------------------------
        var isValidColor = false;
        if(colorRules.isEmpty()) isValidColor = true
        for (rule in colorRules) {

            val hasFirst = outfitColors.contains(rule.first.lowercase())
            val hasSecond = outfitColors.contains(rule.second.lowercase())

            if(hasFirst && hasSecond) {
                isValidColor = true
                break
            }
        }

        // ------------------------------------------------------
        // 2️⃣ RÈGLES DE VÊTEMENTS
        // ------------------------------------------------------
        var isValidCloth = false
        val outfitIds = listOfNotNull(
            outfit.global?.id?.toString(),
            outfit.bas?.id?.toString(),
            outfit.haut?.id?.toString(),
            outfit.chaussures?.id?.toString(),
            outfit.manteau?.id?.toString(),
            outfit.teeShirt?.id?.toString()
        )
        if(clotheRules.isEmpty()) isValidCloth = true
        for (rule in clotheRules) {

            val hasFirst = outfitIds.contains(rule.first)
            val hasSecond = outfitIds.contains(rule.second)

            if(hasFirst && hasSecond) {
                isValidCloth = true
                break
            }
        }

        return isValidCloth && isValidColor
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

    fun saveClothes(
        context: Context,
        sourceUri: Uri,
        category: String,
        subCategory: String?,
        color: String,
        seasons: List<String>,
        motif: String,
        nom: String,
        onDone: () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {

            val localPath = copyImageToInternalStorage(context, sourceUri)

            addClothes(
                category = category,
                subCategory = subCategory,
                color = color.trim(),
                season = seasons.joinToString("-"),
                imageUri = localPath,
                motif = motif,
                nom = nom
            )

            withContext(Dispatchers.Main) {
                onDone()
            }
        }
    }
    fun generateOutfit(
        generatGlobalOutfit: Boolean,
        generatGiletWithTeeShirt: Boolean,
        rules: List<Rule>,
        season: String
    ) {
        viewModelScope.launch(Dispatchers.Default) {
            val result = getRandomOutfit(
                generatGlobalOutfit,
                generatGiletWithTeeShirt,
                rules,
                season
            )
            _outfit.value = result
        }
    }
}
