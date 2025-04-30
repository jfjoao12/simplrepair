package com.example.project_simplrepair.Models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "invoice_table",
    foreignKeys = [
        ForeignKey(
            entity = Repair::class,
            parentColumns = ["id"],
            childColumns = ["repair_id"]
        ),
        ForeignKey(
            entity = Customer::class,
            parentColumns = ["id"],
            childColumns = ["customer_id"]
        )
    ]
)
data class Invoice (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "repair_id")
    val repairId: Int,

    @ColumnInfo(name = "customer_id")
    val customerId: Int,

    @ColumnInfo(name = "payment_method")
    val paymentMethod: PaymentMethods,

    @ColumnInfo(name = "reference")
    val string: String,

    @ColumnInfo(name = "Subtotal")
    val subTotal: Double,

    @ColumnInfo(name = "total")
    val total: Double,

)

enum class PaymentMethods (val displayName: String){
    VISA("Visa"),
    AMEX("Amex"),
    MASTERCARD("Mastercard"),
    DEBIT("Debit"),
    CASH("Cash")
}