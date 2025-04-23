package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician

@Dao
interface RepairDAO {
    @Insert (onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(repair: Repair)

    @Query ("SELECT * FROM repairs_table WHERE id = :id")
    suspend fun getRepairById(id: Int): Repair?

    @Query ("SELECT * FROM repairs_table")
    suspend fun getAllRepairs(): List<Repair>

    @Query ("""
            SELECT * FROM customers_table 
            INNER JOIN repairs_table ON repairs_table.customer_id = customers_table.id
            WHERE repairs_table.id = :id
            """)
    suspend fun getCustomerByRepairId(id: Int): Customer

    @Query ("""
                SELECT * FROM technician_table 
                INNER JOIN repairs_table ON repairs_table.technician_id = technician_table.id
                WHERE repairs_table.id = :id
            """)
    suspend fun getTechByRepairId(id: Int): Technician

    @Query ("""
                SELECT * FROM device_table
                INNER JOIN repairs_table ON repairs_table.technician_id = device_table.id
                WHERE repairs_table.id = :id
            """)
    suspend fun getDeviceByRepairId(id: Int): Device
}