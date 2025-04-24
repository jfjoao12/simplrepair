package com.example.project_simplrepair.Operations

import kotlin.math.round

/**
 * Calculates the total price of a repair including GST (5%) and PST (7%).
 *
 * @param price The subtotal (pre-tax) price of the repair.
 * @return The total price after tax, rounded to two decimal places.
 */
fun taxesCalculation(price: Double): Double {
    val GST = price * 0.05
    val PST = price * 0.07
    val total = price + GST + PST
    return round(total * 100) / 100.0
}

/**
 * Formats a repair ID into a readable string with a "RP" prefix and padded zeroes.
 *
 * @param id The numeric ID of the repair.
 * @return A formatted ID string, e.g., "RP000012".
 */
fun showRepairID(id: Int?): String {
    val idPrefix = "RP00000"
    val idCount = id.toString().count()
    return "${idPrefix.dropLast(idCount)}${id}"
}
