package com.example.project_simplrepair.Models

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import androidx.room.TypeConverters

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
@Entity(tableName = "phone_details")
@TypeConverters(Converters::class)
data class PhoneDetails(

    @PrimaryKey val id: Int,
    val name: String,

    @Embedded(prefix = "brand_")
    val brand: BrandInfo,

    @Embedded(prefix = "battery_")
    val battery: BatteryInfo,

    @Embedded(prefix = "platform_")
    val platform: PlatformInfo,

    @Embedded(prefix = "network_")
    val network: NetworkInfo,

    @Embedded(prefix = "display_")
    val display: DisplayInfo,

    @Embedded(prefix = "launch_")
    val launch: LaunchInfo,

    @Embedded(prefix = "memory_")
    val memory: MemoryInfo,

    @Embedded(prefix = "comms_")
    val comms: CommsInfo,

    @Embedded(prefix = "features_")
    val features: FeaturesInfo,

    // these two use your converter to store as JSON text
    val colors: List<String>,
    val models: List<String>,

    // camera info is nested twice—flatten both into columns
    @Embedded(prefix = "mainCam_")
    val mainCamera: SubCamera,

    @Embedded(prefix = "selfieCam_")
    val selfieCamera: SubCamera,

    @Json(name = "2g") val twoG: String,
    @Json(name = "3g") val threeG: String,
    @Json(name = "4g") val fourG: String,
    @Json(name = "5g") val fiveG: String
)

