package com.example.project_simplrepair.Operations

enum class InventoryTypes(var displayName: String) {
    PART("Part"),
    ACCESSORY("Accessory"),
    REFURB("Refurb"),
    SERVICE("Service"),
    MISC("Miscellaneous")
}

enum class InventorySubType(
    val parent: InventoryTypes,
    var displayName: String
) {
    // PART subtypes
    DISPLAY   (InventoryTypes.PART, "Display"),
    BATTERY   (InventoryTypes.PART, "Battery"),
    CAMERA    (InventoryTypes.PART, "Camera"),
    OTHER     (InventoryTypes.PART, "Other"),

    // ACCESSORY subtypes
    SCREEN_PROTECTOR(InventoryTypes.ACCESSORY, "Screen Protector"),
    CASE         (InventoryTypes.ACCESSORY, "Case"),
    CABLE        (InventoryTypes.ACCESSORY, "Cable"),
    STORAGE      (InventoryTypes.ACCESSORY, "Storage"),
    CHARGER      (InventoryTypes.ACCESSORY, "Charger"),
    ACC_OTHER    (InventoryTypes.ACCESSORY, "Other"),

    // SERVICE subtypes
    LABOUR       (InventoryTypes.SERVICE,  "Labour"),
    CLEANING     (InventoryTypes.SERVICE,  "Cleaning"),
    CUSTOMER_SVC (InventoryTypes.SERVICE,  "Customer Service"),

    // REFURB/MISC have no further split, but we give them “self” entries
    REFURB       (InventoryTypes.REFURB,   "Refurb"),
    MISC         (InventoryTypes.MISC,     "Miscellaneous");

    companion object {
        /** All of them, so you can loop or build form lists. */
        val values = values().toList()

        /** Helpers to get just a single parent’s children: */
        fun forParent(p: InventoryTypes) =
            values.filter { it.parent == p }
    }
}


