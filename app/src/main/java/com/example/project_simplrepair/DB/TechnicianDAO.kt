package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Technician

@Dao
interface TechnicianDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(technician: Technician)

    @Query("""
        SELECT technician_name FROM technician_table
        JOIN repairs_table ON repairs_table.technician_id = technician_table.id
        WHERE repairs_table.technician_id = :id
    """)
    fun getTechNameById(id: Int): String
}