package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "device_photos",
    foreignKeys = [
        ForeignKey (
            entity = Repair::class,
            parentColumns = ["id"],
            childColumns = ["repair_id"]
        )
    ]
)
data class DevicePhoto (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val photoId: Int?,

    @ColumnInfo(name = "repair_id")
    val repairId: Int?,

    @ColumnInfo(name = "path")
    val filePath: String,

    @ColumnInfo(name = "timestamp")
    val timestamp: Long = System.currentTimeMillis()

)