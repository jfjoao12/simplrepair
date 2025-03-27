
package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass



@Entity(tableName = "phone_brands")
@JsonClass(generateAdapter = true)
data class PhoneBrands (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "brand_id")
    val id: Int = 0, // Default value added
    @Json(name = "brandValue")
    @ColumnInfo(name = "brand_type")
    val brandValue: String
)