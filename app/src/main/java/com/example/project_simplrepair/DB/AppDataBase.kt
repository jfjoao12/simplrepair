package com.example.project_simplrepair.DB

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.project_simplrepair.Models.*
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

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
        DevicePhoto::class,
        Inventory::class,
        Invoice::class,
    ],
    version = 41,
)
//abstract class SimplDatabase : RoomDatabase() {
//
//    @DeleteColumn(
//        tableName  = "invoice_table",
//        columnName = "amount_paid"
//    )
//    @RenameColumn(
//        tableName       = "invoice_table",
//        fromColumnName  = "amount_paid",
//        toColumnName    = "total"
//    )
//    class Migration38To39 : AutoMigrationSpec
//}

@TypeConverters(Converters::class)
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

    abstract fun inventoryDao(): InventoryItemDAO

    abstract fun invoiceDao(): InvoiceDAO


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
                    //.addMigrations(MIGRATION)
                    //.createFromAsset("import_db.db") // Pre-populates from asset if available
                    .fallbackToDestructiveMigration() // Drops and recreates DB on version mismatch
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}