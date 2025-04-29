package com.example.project_simplrepair.DB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.project_simplrepair.Models.*

/**
 * Main Room database class for the Simpl Repair application.
 * Holds DAOs for all tables and sets up the database configuration.
 */
@Database(
    entities = [
        Repair::class,
        Customer::class,
        Device::class,
        PhoneBrands::class,
        PhoneSpecs::class,
        Technician::class,
        DevicePhoto::class
    ],
    version = 33,
    exportSchema = true,
)
@TypeConverters(Converters::class) // Uncomment if you add custom type converters
abstract class AppDatabase : RoomDatabase() {

    /** Data access object for repairs table */
    abstract fun repairDAO(): RepairDAO

    /** DAO for phone brands */
    abstract fun phoneBrandsDAO(): PhoneBrandsDAO

    /** DAO for phone models */
    abstract fun phoneModelsDAO(): PhoneModelsDAO

    /** DAO for phone specifications */
    abstract fun phoneSpecsDAO(): PhoneSpecsDAO

    /** DAO for customer information */
    abstract fun customerDao(): CustomerDAO

    /** DAO for device information */
    abstract fun deviceDao(): DeviceDAO

    /** DAO for technician information */
    abstract fun technicianDao(): TechnicianDAO

    /** DAO for device photo */
    abstract fun devicePhotoDao(): DevicePhotoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Returns the singleton instance of the database. Initializes the database if not already created.
         *
         * @param context The application context
         * @return Instance of [AppDatabase]
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Simpl Database"
                )
                    //.createFromAsset("import_db.db") // Pre-populates from asset if available
                    .fallbackToDestructiveMigration() // Drops and recreates DB on version mismatch
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}