package com.example.dressmeapp.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.dressmeapp.model.Outfit

@Dao
interface OutfitDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOutfit(outfit: Outfit)

    @Query("SELECT * FROM outfit ORDER BY id DESC")
    fun getAllOutfits(): LiveData<List<Outfit>>

    @Query("SELECT * FROM outfit WHERE id = :id")
    suspend fun getOutfitById(id: Int): Outfit?

    @Delete
    suspend fun deleteOutfit(outfit: Outfit)

}