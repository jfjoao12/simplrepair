package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

/**
 * Represents the specifications of a phone.
 *
 * @property id                 Auto-generated primary key.
 * @property year               The release year of the phone model.
 * @property brand              The brand of the phone (e.g., "Samsung", "Apple").
 * @property modelName          The name of the phone model (e.g., "Galaxy S21").
 * @property launchDate         The launch date of the phone model.
 * @property chipset            The chipset used in the phone.
 * @property cpu                The CPU model of the phone.
 * @property gpu                The GPU used in the phone.
 * @property mainCameraFeatures Features of the main camera (e.g., "Quad camera").
 * @property mainCameraSpecs    Specifications of the main camera (e.g., "12 MP").
 * @property mainCameraVideo    Video capabilities of the main camera (e.g., "4K@30fps").
 */
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
