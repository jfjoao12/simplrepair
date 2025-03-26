package com.example.project_simplrepair.Models

import androidx.room.*
import com.example.project_simplrepair.Operations.RepairType

@Entity
//@JsonClass(generateAdapter = true)
data class Repair(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val model: String,
    val serial: String,
    val type: String,
    val costumerName: String,
    val technicianName: String,
    val price: Double,
    val repairType: RepairType
)



