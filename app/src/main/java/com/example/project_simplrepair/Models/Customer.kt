package com.example.project_simplrepair.Models

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Customer (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
//    @ForeignKey()
    val device_id: Int,
    val customer_name: String,
    val customer_email: String,
    val customer_city: String,
    val customer_country: String,
    val customer_postalcode: String,
    val customer_prov: String,
    val customer_address: String,
    val customer_address_two: String,
    val customer_phone: String,
    val customer_phone_two: String
)