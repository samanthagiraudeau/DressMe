
package com.example.dressmeapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dressmeapp.model.Clothes

@Dao
interface ClothesDao {
    @Insert
    suspend fun insert(clothes: Clothes)

    @Query("SELECT * FROM clothes ORDER BY id DESC")
    fun getAllClothes(): LiveData<List<Clothes>>

    @Query("SELECT * FROM clothes WHERE category = :category")
    suspend fun getByType(category: String): List<Clothes>

    @Query("""SELECT * FROM clothes WHERE category = :category AND season LIKE '%' || :season || '%' """)

    suspend fun getByTypeAndSeason(category: String, season: String): List<Clothes>

    @Query("""SELECT * FROM clothes WHERE category = 'haut' AND (subCategory = 'pull' OR subCategory = 'gilet') AND season LIKE '%' || :season || '%' """)
    suspend fun getPullEtGilet(season: String): List<Clothes>

    @Query("""SELECT * FROM clothes WHERE category = 'haut' AND subCategory = 'tee-shirt' AND season LIKE '%' || :season || '%' """)
    suspend fun getTeeShirts(season: String): List<Clothes>
    @Delete
    suspend fun delete(clothes: Clothes)

}
