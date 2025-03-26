package com.example.project_simplrepair.Destination



sealed class Destination (val route: String) {
    object Main : Destination("Repairs")
    object Settings : Destination("Settings")
    object Inventory : Destination("Inventory")
    object Appointments : Destination("Appointments")
    object Search : Destination("Search")
    object RepairDetails : Destination("Repair Details")
    object NewRepair : Destination("Create new repair")
}