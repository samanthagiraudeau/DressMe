package com.example.dressmeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rules")
data class Rule(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "color" ou "clothes"
    val first: String,
    val second: String
)
