package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.project_simplrepair.Models.Device

@Dao
interface DeviceDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(device: Device): Long

    @Query ("""
                SELECT phone_model_name FROM phone_models_table
                JOIN device_table ON device_table.phone_model_id = phone_models_table.id
                WHERE device_table.id = :id
            """)
    fun getModelNameByDeviceId(id: Int): String?
}