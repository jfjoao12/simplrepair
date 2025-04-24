package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Technician

/**
 * Data Access Object for managing [Technician] records.
 */
@Dao
interface TechnicianDAO {

    /**
     * Inserts a [Technician] into the database.
     * Ignores the insertion if a conflict occurs (e.g., duplicate ID).
     *
     * @param technician The technician to insert.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(technician: Technician)

    /**
     * Retrieves a technician's name by their ID from the associated repair.
     *
     * @param id The ID of the technician.
     * @return The technician's name as a [String].
     */
    @Query("""
        SELECT technician_name FROM technician_table
        JOIN repairs_table ON repairs_table.technician_id = technician_table.id
        WHERE repairs_table.technician_id = :id
    """)
    fun getTechNameById(id: Int): String
}
