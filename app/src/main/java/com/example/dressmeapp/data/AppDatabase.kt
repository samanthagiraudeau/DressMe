
package com.example.dressmeapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.dressmeapp.model.Clothes
import com.example.dressmeapp.model.Rule
import com.example.dressmeapp.model.Outfit
import com.example.dressmeapp.utils.Converters

@Database(entities = [Clothes::class, Rule::class, Outfit::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun clothesDao(): ClothesDao
    abstract fun rulesDao(): RulesDao
    abstract fun outfitDao(): OutfitDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dressing.db"
                )
                    .fallbackToDestructiveMigration() // ⬅️ en DEV, évite les crashes
                    .build().also { INSTANCE = it }
            }
    }
}
