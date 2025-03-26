package com.example.project_simplrepair.Operations

import kotlin.math.round

/**
 * Calculates the taxes for the repair
 * price: the subtotal of the repair
 */
fun taxesCalculation(price: Double): Double {
    val GST = price * 0.05
    val PST = price * 0.07
    val total = price + GST + PST
    return round(total * 100) / 100.0

}

fun showRepairID(id: Int): String {
    val idPrefix = "RP00000"
    val idCount = id.toString().count()

    return "${idPrefix.dropLast(idCount)}${id}"
}