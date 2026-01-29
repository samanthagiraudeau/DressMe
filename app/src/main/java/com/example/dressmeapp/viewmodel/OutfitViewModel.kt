
package com.example.dressmeapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dressmeapp.data.AppDatabase
import com.example.dressmeapp.data.ClothesRepository
import com.example.dressmeapp.data.OutfitRepository
import kotlinx.coroutines.launch

import com.example.dressmeapp.model.Outfit


class OutfitViewModel(application: Application) : AndroidViewModel(application) {
    private val repo: OutfitRepository

    init {
        val db = AppDatabase.getInstance(application)
        repo = OutfitRepository(db.outfitDao())
    }

    val allOutfits = repo.getAllOutfits()

    fun saveOutfit(name: String, seasons: List<String>, clothesIds: List<Int>) {
        val csv = clothesIds.joinToString(",")
        val outfit = Outfit(
            nom = name,
            seasons = seasons,
            clothesIds = csv
        )

        viewModelScope.launch {
            repo.insertOutfit(outfit)
        }
    }

    fun deleteOutfit(outfit: Outfit) {
        viewModelScope.launch {
            repo.deleteOutfit(outfit)
        }
    }
}
