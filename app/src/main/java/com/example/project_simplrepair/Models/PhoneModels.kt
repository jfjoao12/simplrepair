package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import com.example.project_simplrepair.Operations.RepairType
import com.squareup.moshi.Json

@Entity // (tableName = "repairs_table")
    (
    tableName = "phone_models_table",
    foreignKeys = [
        ForeignKey(
            entity = PhoneBrands::class,
            parentColumns = arrayOf("brand_id"),
            childColumns = arrayOf("brand_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class PhoneModels(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,

    @ColumnInfo(name = "brand_id") // ✅ Better naming for clarity
    val brandId: Int = 0,

    @Json(name = "modelValue")
    @ColumnInfo(name = "phone_model_name")
    val phoneModelName: String
)