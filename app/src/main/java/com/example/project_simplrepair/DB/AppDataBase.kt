package com.example.project_simplrepair.DB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.project_simplrepair.Models.Repair

@Database(entities = [Repair::class], version = 3, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun repairDAO(): RepairDAO

    // COMPANION OBJECT
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase ?= null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Simpl Database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return INSTANCE ?: throw IllegalStateException("Database not initialized")

            }
        }
    }
}