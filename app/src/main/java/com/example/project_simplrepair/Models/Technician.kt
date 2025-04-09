package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Add new columns later!!
@Entity(tableName = "technician_table")
data class Technician(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,
    @ColumnInfo(name = "technician_name")
    val name: String
)