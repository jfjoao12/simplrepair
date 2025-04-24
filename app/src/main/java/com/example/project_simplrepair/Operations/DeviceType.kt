package com.example.project_simplrepair.Operations

/**
 * Enum class representing different types of devices supported by the repair system.
 *
 * @property displayName The user-friendly name of the device type.
 */
enum class DeviceType(val displayName: String) {
    MOBILE("Mobile Phone"),
    LAPTOP("Laptop"),
    DESKTOP("Desktop"),
    WATCH("Watch"),
    MISC("Miscellaneous")
}
