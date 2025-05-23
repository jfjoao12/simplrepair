package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.PhoneSpecs
import kotlinx.coroutines.flow.Flow

/**
 * DAO for managing phone model data in the Room database.
 */
@Dao
interface PhoneModelsDAO {

    /**
     * Inserts a list of [PhoneSpecs] into the database.
     * If a conflict occurs, existing entries will be replaced.
     *
     * @param phoneModels The list of models to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(phoneModels: List<PhoneSpecs>)

    /**
     * Retrieves a phone model by its associated brand ID.
     *
     * @param id The brand ID.
     * @return A [PhoneSpecs] instance or null if not found.
     */
    @Query("SELECT * FROM phone_specs_table WHERE brand_id = :id")
    fun getSpecsByBrandId(id: Int): PhoneSpecs?

    /**
     * Retrieves the brand name associated with a given brand ID.
     *
     * @param id The brand ID to search for.
     * @return The name of the brand.
     */
    @Query("""
        SELECT phone_brands.brand_name 
        FROM phone_brands
        INNER JOIN phone_specs_table
        ON phone_brands.id = phone_specs_table.brand_id
        WHERE phone_brands.id = :id
    """)
    fun getBrandNameById(id: Int): String

    /**
     * Checks whether any phone models exist in the database.
     *
     * @return The total number of records in the table.
     */
    @Query("SELECT COUNT(*) FROM phone_specs_table")
    fun checkIfExists(): Int

    /**
     * Retrieves all phone models that match the provided name.
     * This search is case-insensitive and partial.
     *
     * @param phoneName The name (or partial name) of the phone to search.
     * @return A [Flow] emitting a list of matching [PhoneSpecs].
     */
    @Query("SELECT * FROM phone_specs_table WHERE name LIKE '%' || :phoneName || '%'")
    fun getModelByName(phoneName: String): Flow<List<PhoneSpecs>>
}
