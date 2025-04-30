package com.example.project_simplrepair.Screens.Inventory

import android.view.animation.Animation
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.CustomCardLayout
import com.example.project_simplrepair.Layouts.InfoCard
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Inventory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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

    var searchParam by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Inventory>>(emptyList()) }
    var expandItemCard by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            // Announce this whole region as the Inventory screen
            .semantics { contentDescription = "Inventory Screen" }
    ) {
        Column {
            ScreenTitle("Inventory")

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {

                item {
                    TextField(
                        value = searchParam,
                        onValueChange = { searchParam = it },
                        label = { Text("Search") },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    GlobalScope.launch {
                                        searchResults = db.inventoryDao().search(searchParam)
                                    }
                                }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search models"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

                items(items = searchResults) { item ->
                    InfoCard(
                        item.name,
                        item,
                        visible = {
                            // Always visible summary
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Quantity:", style = MaterialTheme.typography.bodySmall)
                                Text(item.quantity.toString(), textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Price:", style = MaterialTheme.typography.bodySmall)
                                Text("${item.price}", textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall)
                            }
                        },
                        expanded = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("SKU:", style = MaterialTheme.typography.bodySmall)
                                Text(item.sku, textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall)
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Type:", style = MaterialTheme.typography.bodySmall)
                                Text(item.type.displayName, textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Subtype:", style = MaterialTheme.typography.bodySmall)
                                Text(item.subType.displayName, textAlign = TextAlign.End, style = MaterialTheme.typography.bodySmall)
                            }

                        }
                    )
                }
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
