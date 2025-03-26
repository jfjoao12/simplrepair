package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Customer (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "customer_id") val customerId: Int,
    @ColumnInfo(name = "device_id") val deviceId: Int,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_email") val customerEmail: String,
    @ColumnInfo(name = "customer_city") val customerCity: String,
    @ColumnInfo(name = "customer_country") val customerCountry: String,
    @ColumnInfo(name = "customer_postal_code") val customerPostalCode: String,
    @ColumnInfo(name = "customer_prov") val customerProv: String,
    @ColumnInfo(name = "customer_address") val customerAddress: String,
    @ColumnInfo(name = "customer_address_two") val customerAddressTwo: String,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    @ColumnInfo(name = "customer_phone_two") val customerPhoneTwo: String
)