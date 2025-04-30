package com.example.project_simplrepair.Destination



sealed class Destination (val route: String) {
    object Main : Destination("Repairs")
    object Settings : Destination("Settings")
    object Inventory : Destination("Inventory")
    object Appointments : Destination("Appointments")
    object Search : Destination("Search")
    object RepairDetails : Destination("repair_details/{repair_id}")
    object NewRepair : Destination("insertRepair")
    object NewCustomer: Destination("Add new customer")
    object Camera: Destination("Camera")
    object NewInventoryItem: Destination("New Inventory Item")
    object Invoice : Destination("invoice/{repair_id}")
}