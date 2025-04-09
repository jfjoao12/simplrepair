package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.project_simplrepair.Models.Technician

@Dao
interface TechnicianDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(technician: Technician)
}