package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.PhoneBrands
import kotlinx.coroutines.flow.Flow

/**
 * DAO for accessing and managing phone brand data in the local database.
 */
@Dao
interface PhoneBrandsDAO {

    /**
     * Inserts a list of phone brands into the database.
     * Ignores any conflicts (e.g. duplicate brand IDs).
     *
     * @param phoneBrands List of [PhoneBrands] to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(phoneBrands: List<PhoneBrands>)

    /**
     * Retrieves all phone brand entries.
     *
     * @return A list of all [PhoneBrands].
     */
    @Query("SELECT * FROM phone_brands")
    fun getAll(): List<PhoneBrands>

    /**
     * Retrieves the names of all phone brands as a flow for observation.
     *
     * @return A [Flow] emitting a list of all brand names.
     */
    @Query("SELECT brand_name FROM phone_brands")
    fun getAllNames(): Flow<List<String>>

    /**
     * Retrieves the IDs of all phone brands.
     *
     * @return A list of brand IDs.
     */
    @Query("SELECT id FROM phone_brands")
    fun getAllIds(): List<Int>

    /**
     * Retrieves a phone brand by its ID.
     *
     * @param id The ID of the brand to retrieve.
     * @return The matching [PhoneBrands], or null if not found.
     */
    @Query("SELECT * FROM phone_brands WHERE id = :id")
    fun getBrandById(id: Int): PhoneBrands?

    /**
     * Clears all data from the phone brands table.
     */
    @Query("DELETE FROM phone_brands")
    fun clearTable()
}
