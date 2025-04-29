package com.example.project_simplrepair.Screens.Inventory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.ScreenTitle

/**
 * A placeholder screen for showing the inventory.
 *
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param paddingValues Insets to apply for status/navigation bars.
 */
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavController,
    db: AppDatabase,
) {

    var searchParam by remember { mutableStateOf("Search") }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            // Announce this whole region as the Inventory screen
            .semantics { contentDescription = "Inventory Screen" }
    ) {
        Column {

            Box(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                ScreenTitle("Inventory")

                TextField(
                    value = searchParam,
                    onValueChange = { searchParam = it },
                    label = { Text("Search") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {/* Implement */ }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search models"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .semantics { contentDescription = "Insert new item to inventory" },
            onClick = {
                navController.navigate(Destination.NewInventoryItem.route)
            },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,

        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
        }

    }
}
