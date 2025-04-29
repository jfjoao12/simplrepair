//package com.example.project_simplrepair.Models
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import androidx.room.ForeignKey
//import com.squareup.moshi.Json
//
///**
// * Represents a phone model.
// *
// * @property id            Auto-generated primary key.
// * @property brandId       The ID of the associated phone brand (foreign key to [PhoneBrands]).
// * @property phoneModelName The name of the phone model (e.g., "Galaxy S21", "iPhone 12").
// *
// * Foreign Key:
// * - [brandId] references the [PhoneBrands] table's primary key.
// */
//@Entity(
//    tableName = "phone_models_table",
//    foreignKeys = [
//        ForeignKey(
//            entity = PhoneBrands::class,
//            parentColumns = arrayOf("brand_id"),
//            childColumns = arrayOf("brand_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.SET_NULL
//        )
//    ]
//)
//data class Phones(
//    @PrimaryKey(autoGenerate = true) val id: Int = 0,
//
//    @ColumnInfo(name = "brand_id")
//    val brandId: Int = 0,
//
//    @Json(name = "modelValue")
//    @ColumnInfo(name = "phone_model_name")
//    val phoneModelName: String
//)
