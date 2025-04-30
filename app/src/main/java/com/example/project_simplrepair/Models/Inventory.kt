package com.example.project_simplrepair.Models


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project_simplrepair.Operations.InventorySubType
import com.example.project_simplrepair.Operations.InventoryTypes


@Entity(tableName = "inventory_table")
data class Inventory (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "type")
    val type: InventoryTypes,

    @ColumnInfo(name= "subType")
    val subType: InventorySubType,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "sku")
    val sku: String,

    @ColumnInfo(name = "price")
    val price: Double,

    @ColumnInfo(name = "quantity")
    val quantity: Int,
)