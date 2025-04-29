package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents a phone brand.
 *
 * @property id          Auto-generated primary key.
 * @property brandValue  The name of the brand (e.g., "Samsung", "Apple", "Google").
 */

@JsonClass(generateAdapter = true)
data class PhoneListResponseDto(
    @Json(name = "items") val items: List<PhoneBrands>
)

@Entity(tableName = "phone_brands")
@JsonClass(generateAdapter = true)
data class PhoneBrands(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "brand_id")
    val id: Int = 0,

    @Json(name = "name")
    @ColumnInfo(name = "brand_name")
    val brandValue: String
)
