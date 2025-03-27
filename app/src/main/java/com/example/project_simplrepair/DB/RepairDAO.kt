package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Repair

@Dao
interface RepairDAO {
    @Insert (onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(repair: Repair)

    @Query ("SELECT * FROM repairs_table WHERE repair_id = :id")
    suspend fun getRepairById(id: Int): Repair?

    @Query ("SELECT * FROM repairs_table")
    suspend fun getAllRepairs(): List<Repair>

}