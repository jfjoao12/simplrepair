package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.project_simplrepair.DB.CustomerDAO
import com.example.project_simplrepair.DB.RepairDAO
import javax.inject.Inject

/**
 * A customer of the shop.
 *
 * @property customerId       Auto-generated primary key. Null if not yet persisted.
 * @property customerName     Full name of the customer.
 * @property customerEmail    Optional email address.
 * @property customerCity     City name (defaults to “Winnipeg”).
 * @property customerCountry  Country name (defaults to “Canada”).
 * @property customerPostalCode  Postal code.
 * @property customerProv     Province or region (defaults to “MB”).
 * @property customerAddress  First line of street address, if any.
 * @property customerAddressTwo  Second line of street address, if any.
 * @property customerPhone    Primary phone number.
 * @property customerPhoneTwo Optional secondary phone number.
 */
@Entity(tableName = "customers_table")
data class Customer(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val customerId: Int?,

    @ColumnInfo(name = "customer_name")
    val customerName: String,

    @ColumnInfo(name = "customer_email")
    val customerEmail: String?,

    @ColumnInfo(name = "customer_city")
    val customerCity: String = "Winnipeg",

    @ColumnInfo(name = "customer_country")
    val customerCountry: String = "Canada",

    @ColumnInfo(name = "customer_postal_code")
    val customerPostalCode: String,

    @ColumnInfo(name = "customer_prov")
    val customerProv: String = "MB", // In the future, replace with GPS‐derived region

    @ColumnInfo(name = "customer_address")
    val customerAddress: String?,

    @ColumnInfo(name = "customer_address_two")
    val customerAddressTwo: String?,

    @ColumnInfo(name = "customer_phone")
    val customerPhone: String,

    @ColumnInfo(name = "customer_phone_two")
    val customerPhoneTwo: String?
)
