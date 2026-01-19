
package com.example.dressmeapp.data

import com.example.dressmeapp.model.Clothes

class ClothesRepository(private val dao: ClothesDao) {
    val allClothes = dao.getAllClothes()
    suspend fun insert(item: Clothes) = dao.insert(item)
    suspend fun getByType(type: String) = dao.getByType(type)
    suspend fun getByTypeAndSeason(type: String, season: String) = dao.getByTypeAndSeason(type, season)
    suspend fun getPullEtGilet(season: String) = dao.getPullEtGilet(season)
    suspend fun getTeeShirts(season: String) = dao.getTeeShirts(season)

    suspend fun delete(item: Clothes) = dao.delete(item)
}
