package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.TypeConverters
import com.example.project_simplrepair.DB.PhoneSpecsDAO
import com.squareup.moshi.Json
import javax.inject.Inject

/**
 * Represents a phone model.
 *
 * @property id            Auto-generated primary key.
 * @property brandId       The ID of the associated phone brand (foreign key to [PhoneBrands]).
 * @property phoneModelName The name of the phone model (e.g., "Galaxy S21", "iPhone 12").
 *
 * Foreign Key:
 * - [brandId] references the [PhoneBrands] table's primary key.
 */
@Entity(
    tableName = "phone_specs_table",
    foreignKeys = [
        ForeignKey(
            entity = PhoneBrands::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("brand_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        )
    ]
)
@TypeConverters(Converters::class)
data class PhoneSpecs(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    val name: String,

    @Embedded(prefix = "brand_")
    val brand: BrandInfo?,

    @Embedded(prefix = "battery_")
    val battery: BatteryInfo?,

    @Embedded(prefix = "platform_")
    val platform: PlatformInfo?,

    @Embedded(prefix = "network_")
    val network: NetworkInfo?,

    @Embedded(prefix = "display_")
    val display: DisplayInfo?,

    @Embedded(prefix = "launch_")
    val launch: LaunchInfo?,

    @Embedded(prefix = "memory_")
    val memory: MemoryInfo?,

    @Embedded(prefix = "comms_")
    val comms: CommsInfo?,

    @Embedded(prefix = "features_")
    val features: FeaturesInfo?,

    // these two use your converter to store as JSON text
    val colors: List<String>?,
    val models: List<String>?,

    // camera info is nested twice—flatten both into columns
    @Embedded(prefix = "mainCam_")
    val mainCamera: SubCamera?,

    @Embedded(prefix = "selfieCam_")
    val selfieCamera: SubCamera?,

)
