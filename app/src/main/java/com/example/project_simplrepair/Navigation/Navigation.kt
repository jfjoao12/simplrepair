package com.example.project_simplrepair.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.project_simplrepair.Destination.Destination

@Composable
fun BottomNav(navController: NavController) {
    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        // Repairs/main navbar item
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Main.route,
            onClick = {
                navController.navigate(Destination.Main.route) {
                popUpTo(Destination.Main.route)
                launchSingleTop = true
            }
                      },
            icon = {
                Icon(
                    Icons.Rounded.Build,
                    contentDescription = "Repair"
                ) },
            label = { Text(text = Destination.Main.route) }
        )
        // Inventory navbar item
//        NavigationBarItem(
//            selected = currentDestination?.route == Destination.Inventory.route,
//            onClick = {
//                navController.navigate(Destination.Inventory.route) {
//                    popUpTo(Destination.Main.route)
//                    launchSingleTop = true
//                }
//            },
//            icon = {
//                Icon(
//                    Icons.AutoMirrored.Rounded.List,
//                    contentDescription = "Inventory"
//                )
//            },
//            label = {Text(text = Destination.Inventory.route)}
//        )
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Appointments.route,
            onClick = {
                navController.navigate(Destination.Appointments.route) {
                    popUpTo(Destination.Main.route)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Rounded.DateRange,
                    contentDescription = "Appointments"
                )
            },
            label = {Text(text = Destination.Appointments.route)}
        )
    }
}