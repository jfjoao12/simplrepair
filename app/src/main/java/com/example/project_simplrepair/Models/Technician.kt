package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a technician in the database.
 *
 * This entity is used to store details about the technicians, such as their name.
 * It can be referenced in other entities like repairs to associate a repair with a specific technician.
 */
@Entity(tableName = "technician_table")
data class Technician(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int?,

    @ColumnInfo(name = "technician_name")
    val name: String
)
