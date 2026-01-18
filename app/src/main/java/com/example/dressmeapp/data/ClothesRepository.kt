
package com.example.dressmeapp.data

import com.example.dressmeapp.model.Clothes

class ClothesRepository(private val dao: ClothesDao) {
    val allClothes = dao.getAllClothes()
    suspend fun insert(item: Clothes) = dao.insert(item)
    suspend fun getByType(type: String) = dao.getByType(type)
    suspend fun getPullEtGilet() = dao.getPullEtGilet()
    suspend fun getTeeShirts() = dao.getTeeShirts()

    suspend fun delete(item: Clothes) = dao.delete(item)
}
