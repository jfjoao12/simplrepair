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
 * @property brandValue  The name of the brand (e.g., "Samsung", "Apple").
 */
@Entity(tableName = "phone_brands")
@JsonClass(generateAdapter = true)
data class PhoneBrands(
    @PrimaryKey(autoGenerate = true)
    @Json(name = "id")
    @ColumnInfo(name = "id")
    val id: Int = 0,

    @Json(name = "name")
    @ColumnInfo(name = "brand_name")
    val brandValue: String
)

@JsonClass(generateAdapter = true)
data class PhoneBrandsItem(
    @Json(name = "items")
    var item: List<PhoneBrands>
)
