package com.example.project_simplrepair.Operations

import kotlin.math.round

/**
 * Calculates the taxes for the repair
 * price: the subtotal of the repair
 */
fun TaxesCalculation(price: Double): Double {
    val GST = price * 0.05
    val PST = price * 0.07
    val total = price + GST + PST
    return round(total * 100) / 100.0

}