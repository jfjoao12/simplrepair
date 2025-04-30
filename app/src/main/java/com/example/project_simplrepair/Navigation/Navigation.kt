package com.example.project_simplrepair.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.List
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.project_simplrepair.Destination.Destination

/**
 * Bottom navigation bar for the app.
 *
 * Displays the main sections of the app (Repairs, Appointments, Search, etc.) and allows users
 * to navigate between them using icons and labels.
 *
 * @param navController The navigation controller that handles the navigation actions.
 */
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
                    popUpTo(Destination.Main.route) // Ensures that multiple copies of the Main screen don't accumulate
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Rounded.Build,
                    contentDescription = "Repair" // Descriptive content description for accessibility
                )
            },
            label = { Text(text = Destination.Main.route) }
        )

        // Inventory navbar item (commented out, but here for future reference)
        /*
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Inventory.route,
            onClick = {
                navController.navigate(Destination.Inventory.route) {
                    popUpTo(Destination.Main.route)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.AutoMirrored.Rounded.List,
                    contentDescription = "Inventory"
                )
            },
            label = {Text(text = Destination.Inventory.route)}
        )
        */

        // Appointments navbar item
//        NavigationBarItem(
//            selected = currentDestination?.route == Destination.Appointments.route,
//            onClick = {
//                navController.navigate(Destination.Appointments.route) {
//                    popUpTo(Destination.Main.route) // Avoiding stack overflow by popping up to Main route
//                    launchSingleTop = true
//                }
//            },
//            icon = {
//                Icon(
//                    Icons.Rounded.DateRange,
//                    contentDescription = "Appointments" // Descriptive content description for accessibility
//                )
//            },
//            label = { Text(text = Destination.Appointments.route) }
//        )
        // Inventory navbar item
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Inventory.route,
            onClick = {
                navController.navigate(Destination.Inventory.route) {
                    popUpTo(Destination.Main.route)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.AutoMirrored.Rounded.List,
                    contentDescription = "Inventory"
                )
            },
            label = { Text(text = Destination.Inventory.route) }
        )
        // Search navbar item
        NavigationBarItem(
            selected = currentDestination?.route == Destination.Search.route,
            onClick = {
                navController.navigate(Destination.Search.route) {
                    popUpTo(Destination.Main.route)
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    Icons.Rounded.Search,
                    contentDescription = "Search" // Descriptive content description for accessibility
                )
            },
            label = { Text(text = Destination.Search.route) }
        )
    }
}
