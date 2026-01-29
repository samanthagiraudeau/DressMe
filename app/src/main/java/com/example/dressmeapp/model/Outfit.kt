package com.example.dressmeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "outfit")
data class Outfit(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String?,
    val seasons: List<String>,
    val clothesIds: String // CSV: "12,3,8,21"

)
