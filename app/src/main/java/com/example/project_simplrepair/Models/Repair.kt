package com.example.project_simplrepair.Models

import androidx.room.*

@Entity
//@JsonClass(generateAdapter = true)
data class Repair(
    @PrimaryKey
    val id: Int,
    val model: String,
    val serial: Int,
    val type: String,
)

