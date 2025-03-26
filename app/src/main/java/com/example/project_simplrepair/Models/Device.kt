package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project_simplrepair.Operations.DeviceType

@Entity
data class Device (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "device_id") val deviceId: Int,
    @ColumnInfo(name = "device_type") val deviceType: DeviceType,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
    @ColumnInfo(name = "device_manufacturer") val deviceManufacturer: String
)