package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.project_simplrepair.DB.RepairDAO
import com.example.project_simplrepair.Operations.RepairType
import javax.inject.Inject

/**
 * Represents a repair entry in the database.
 *
 * This entity links to the customer, device, and technician tables, and stores repair details such as
 * the repair type, price, and any additional notes related to the repair.
 */
@Entity(
    tableName = "repairs_table",
    foreignKeys = [
        ForeignKey(
            entity = Customer::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("customer_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = Device::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("device_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.NO_ACTION
        ),
        ForeignKey(
            entity = Technician::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("technician_id"),
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.NO_ACTION
        )
    ]
)
data class Repair (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int?,

    @ColumnInfo(name = "customer_id") val customerId: Int? = null,
    @ColumnInfo(name = "device_id") val deviceId: Int? = null,
    @ColumnInfo(name = "technician_id") val technicianId: Int? = null,
    @ColumnInfo(name = "price") val price: Double,
    @ColumnInfo(name = "notes") val notes: String,
    @ColumnInfo(name = "repair_status") val repairStatus: RepairStatus = RepairStatus.IN_REPAIR,
    @ColumnInfo(name = "repair_type") val repairType: RepairType,
    @ColumnInfo(name="image_uri") val imageUri: String? = null

)

enum class RepairStatus (val displayName: String) {
    IN_REPAIR("In Repair"),
    PARTS_ORDERED("Parts ordered"),
    COMPLETED("Completed")
}