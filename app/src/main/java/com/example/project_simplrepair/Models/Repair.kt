package com.example.project_simplrepair.Models

import androidx.room.*
import com.example.project_simplrepair.Operations.RepairType

@Entity (tableName = "repairs_table")
//    (
//    tableName = "repairs_table",
//    foreignKeys = [
//        ForeignKey(
//            entity = Customer::class,
//            parentColumns = arrayOf("customer_id"),
//            childColumns = arrayOf("customer_id"),
//            onUpdate = ForeignKey.CASCADE,
//            onDelete = ForeignKey.SET_NULL
//        )
//    ]
//)
data class Repair(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "repair_id") val id: Int,
    //@ColumnInfo(name = "customer_id") val customerId: Int?,
    @ColumnInfo(name = "model") val model: String,
    @ColumnInfo(name = "serial") val serial: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "customer_name") val costumerName: String,
    @ColumnInfo(name = "technician_name") val technicianName: String,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "repair_type") val repairType: RepairType
)



