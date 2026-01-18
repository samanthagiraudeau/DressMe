package com.example.dressmeapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clothes")
data class Clothes(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nom: String?,
    val category: String,    // "manteau", "haut", "bas"
    val subCategory: String?,    // "pull", "gilet", "pantalon", null
    val color: String,   // ex: "noir", "bleu"
    val motif: String,
    val season: String,  // ex: "été", "hiver", "mi-saison"
    val imageUri: String // URI de la photo (string)
)
