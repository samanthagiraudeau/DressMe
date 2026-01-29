package com.example.dressmeapp.utils
import androidx.room.TypeConverter

class Converters {

    // Liste de saisons <-> String
    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return list.joinToString("§")   // séparateur safe
    }

    @TypeConverter
    fun toStringList(data: String): List<String> {
        if (data.isBlank()) return emptyList()
        return data.split("§")
    }

    // Liste d’IDs <-> String
    @TypeConverter
    fun fromIntList(list: List<Int>): String {
        return list.joinToString(",")
    }

    @TypeConverter
    fun toIntList(data: String): List<Int> {
        if (data.isBlank()) return emptyList()
        return data.split(",").map { it.toInt() }
    }
}