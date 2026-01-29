
package com.example.dressmeapp.data

import com.example.dressmeapp.model.Outfit

class OutfitRepository(private val dao: OutfitDao) {

    fun getAllOutfits() = dao.getAllOutfits()

    suspend fun insertOutfit(outfit: Outfit) = dao.insertOutfit(outfit)

    suspend fun getOutfitById(id: Int) = dao.getOutfitById(id)

    suspend fun deleteOutfit(outfit: Outfit) = dao.deleteOutfit(outfit)
}
