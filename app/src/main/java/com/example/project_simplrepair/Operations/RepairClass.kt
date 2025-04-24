package com.example.project_simplrepair.Operations

/**
 * Enum class representing the classification of a repair.
 *
 * @property displayName A user-friendly label describing the repair class.
 */
enum class RepairClass(val displayName: String) {
    WALKIN("Walk-in"),
    APPOINTMENT("Appointment"),
    WARRANTY("Warranty"),
    REFURB("Refurb")
}
