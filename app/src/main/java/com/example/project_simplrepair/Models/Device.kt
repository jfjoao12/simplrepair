package com.example.project_simplrepair.Models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val device_type:
)