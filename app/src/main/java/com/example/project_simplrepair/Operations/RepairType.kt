package com.example.project_simplrepair.Operations

/**
 * Enum class representing the different types of repairs that can be performed.
 *
 * @property displayName The user-friendly name for the repair type.
 */
enum class RepairType(val displayName: String) {
    BATTERY("Battery"),
    DISPLAY("Display"),
    MISC("Miscellaneous")
}
