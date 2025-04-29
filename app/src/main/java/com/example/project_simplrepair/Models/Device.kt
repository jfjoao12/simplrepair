package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.project_simplrepair.Operations.DeviceType

/**
 * A physical device brought in for repair.
 *
 * @property deviceId       Auto-generated primary key. Null if not yet stored.
 * @property customerId     FK to the owning Customer’s `id`.
 * @property phoneModelId   FK to the PhoneModels table, indicating make/model.
 * @property deviceType     Type of device (e.g. PHONE, TABLET).
 * @property deviceSerial   The device’s serial number or IMEI.
 */
@Entity(
    tableName = "device_table",
    foreignKeys = [
        ForeignKey(
            entity = PhoneSpecs::class,
            parentColumns = ["id"],
            childColumns = ["phone_model_id"]
        ),
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"]
        )
    ]
)
data class Device(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val deviceId: Int?,

    @ColumnInfo(name = "customer_id")
    val customerId: Int?,

    @ColumnInfo(name = "phone_model_id")
    val phoneModelId: Int,

    @ColumnInfo(name = "device_type")
    val deviceType: DeviceType = DeviceType.MOBILE,

    @ColumnInfo(name = "device_serial")
    val deviceSerial: String
)
