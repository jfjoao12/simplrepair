package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phone_specifications")
data class PhoneSpecifications (
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
