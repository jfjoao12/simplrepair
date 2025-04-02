package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.project_simplrepair.Operations.DeviceType

@Entity (
    tableName = "device_table",
    foreignKeys = [
        ForeignKey(
            entity = PhoneModels::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("phone_model_id")
        )
    ]
)
data class Device (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val deviceId: Int?,
    @ColumnInfo(name = "phone_model_id") val phoneModelId : Int,
    @ColumnInfo(name = "device_type") val deviceType: DeviceType = DeviceType.MOBILE,
    @ColumnInfo(name = "device_serial") val deviceSerial: String,
)