package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (
    tableName = "customers_table",
    foreignKeys = [
        ForeignKey (
            entity = Device::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("device_id")
        ),
//        ForeignKey (
//            entity = Repair::class,
//            parentColumns = arrayOf("repair_id"),
//            childColumns = arrayOf("repair_id")
//        )
    ]
)
data class Customer(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val customerId: Int?,
    @ColumnInfo(name = "device_id") val deviceId: Int? = null,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_email") val customerEmail: String?,
    @ColumnInfo(name = "customer_city") val customerCity: String = "Winnipeg",
    @ColumnInfo(name = "customer_country") val customerCountry: String = "Canada",
    @ColumnInfo(name = "customer_postal_code") val customerPostalCode: String,
    @ColumnInfo(name = "customer_prov") val customerProv: String = "MB", // In the future, replace GPS location
    @ColumnInfo(name = "customer_address") val customerAddress: String?,
    @ColumnInfo(name = "customer_address_two") val customerAddressTwo: String?,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    @ColumnInfo(name = "customer_phone_two") val customerPhoneTwo: String?
)