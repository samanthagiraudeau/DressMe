
package com.example.dressmeapp.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule


@Dao
interface RulesDao {
    @Insert
    suspend fun insert(rule: Rule)

    @Delete
    suspend fun delete(rule: Rule)

    @Query("SELECT * FROM rules ORDER BY id DESC")
    fun getAllRules(): LiveData<List<Rule>>

    @Query("SELECT * FROM rules WHERE type = :type")
    fun getByType(type: String): LiveData<List<Rule>>
}
