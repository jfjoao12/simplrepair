package com.example.project_simplrepair.Screens.Inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.CustomCardLayout
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Inventory
import com.example.project_simplrepair.Operations.InventorySubType
import com.example.project_simplrepair.Operations.InventoryTypes
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewInventoryItemScreen(
    paddingValues: PaddingValues,
    navController: NavController,
    db: AppDatabase,
) {

    var expanded by  remember { mutableStateOf(false) }


    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var sku by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }

    // Parent and child selection state
    var selectedType by remember { mutableStateOf<InventoryTypes?>(null) }
    var typeExpanded by remember { mutableStateOf(false) }
    var selectedSubType by remember { mutableStateOf<InventorySubType?>(null) }
    var subExpanded by remember { mutableStateOf(false) }
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
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                ScreenTitle("Add Inventory Item")
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                CustomCardLayout("Info") {
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .semantics { contentDescription = "Item name input" }
                    )

                    OutlinedTextField(
                        value = sku,
                        onValueChange = { sku = it },
                        label = { Text("SKU") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .semantics { contentDescription = "Item SKU input" }
                    )

                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Price") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .semantics { contentDescription = "Item price input" }
                    )

                    OutlinedTextField(
                        value = quantity,
                        onValueChange = { quantity = it },
                        label = { Text("Quantity") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .semantics { contentDescription = "Item quantity to add" }
                    )

                }

                CustomCardLayout("Type") {
                    ExposedDropdownMenuBox(
                        expanded = typeExpanded,
                        onExpandedChange = { typeExpanded = !typeExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedType?.displayName ?: "Select Type",
                            onValueChange = {},
                            label = { Text("Item Type") },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(typeExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .menuAnchor(MenuAnchorType.PrimaryEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = typeExpanded,
                            onDismissRequest = { typeExpanded = false }
                        ) {
                            InventoryTypes.entries.forEach { type ->
                                DropdownMenuItem(
                                    text = { Text(type.displayName) },
                                    onClick = {
                                        selectedType = type
                                        selectedSubType = null
                                        typeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Child dropdown
                    ExposedDropdownMenuBox(
                        expanded = subExpanded,
                        onExpandedChange = { if (selectedType != null) subExpanded = !subExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedSubType?.displayName ?: "Select Type first",
                            onValueChange = {},
                            label = { Text("Item Subtype") },
                            readOnly = true,
                            enabled = (selectedType != null),
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(subExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .menuAnchor(MenuAnchorType.PrimaryEditable)
                        )
                        ExposedDropdownMenu(
                            expanded = subExpanded,
                            onDismissRequest = { subExpanded = false }
                        ) {
                            selectedType?.let { parent ->
                                InventorySubType.forParent(parent).forEach { sub ->
                                    DropdownMenuItem(
                                        text = { Text(sub.displayName) },
                                        onClick = {
                                            selectedSubType = sub
                                            subExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Column (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ){
            FloatingActionButton(
                onClick = {
                    GlobalScope.launch {
                        val newInventoryItem =
                            Inventory(
                                id = null,
                                type = selectedType!!,
                                subType = selectedSubType!!,
                                name = name,
                                sku = sku,
                                price = price.toDouble(),
                                repairId = null,
                                quantity = quantity.toInt()
                            )
                        db.inventoryDao().insert(newInventoryItem)
                    }
                    navController.navigate(Destination.Inventory.route)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .semantics { contentDescription = "Insert new item to inventory" }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
    }
}