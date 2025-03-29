package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity(tableName = "phone_specifications")
data class PhoneSpecs(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    // phoneDetails
    @ColumnInfo(name = "year") val year: String?,
    @ColumnInfo(name = "brand") val brand: String?,
    @ColumnInfo(name = "model_name") val modelName: String?,

    // gsmLaunchDetails
    @ColumnInfo(name = "launch_date") val launchDate: String?,

    // ssmPlatformDetails
    @ColumnInfo(name = "chipset") val chipset: String?,
    @ColumnInfo(name = "cpu") val cpu: String?,
    @ColumnInfo(name = "gpu") val gpu: String?,

    // gsmMainCameraDetails
    @ColumnInfo(name = "main_camera_features") val mainCameraFeatures: String?,
    @ColumnInfo(name = "main_camera_specs") val mainCameraSpecs: String?,
    @ColumnInfo(name = "main_camera_video") val mainCameraVideo: String?
)

