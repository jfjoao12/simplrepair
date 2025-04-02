package com.example.project_simplrepair.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.project_simplrepair.Models.Device

@Dao
interface DeviceDAO {
    @Insert (onConflict = OnConflictStrategy.IGNORE)
    fun insert(device: Device)
}