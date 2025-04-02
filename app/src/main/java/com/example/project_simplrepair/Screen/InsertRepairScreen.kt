package com.example.project_simplrepair.Screen

import android.provider.Settings.Global
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Operations.RepairType
import kotlinx.coroutines.GlobalScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertRepairScreen(paddingValues: PaddingValues, db: AppDatabase, navController: NavController) {
    var id by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var costumerName by remember { mutableStateOf("") }
    var technicianName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(RepairType.BATTERY) } // Default selection

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Bottom Sheet State
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            Text(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "New Repair"
            )
            // Modal Bottom sheet
            Column(
                modifier = Modifier.padding(horizontal = 50.dp)
            ) {
                Button(
                    onClick = {
                        showBottomSheet = true
                    }
                ) {
                    Text("Select Brand")
                }
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable { showBottomSheet = true }, // Opens Bottom Sheet
                    value = brand,
                    onValueChange = {},
                    label = { Text("Brand") },
                    readOnly = true,
                    enabled = false,
                    colors = OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        disabledContainerColor = MaterialTheme.colorScheme.surface // Use the same as your enabled background
                    )
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = model,
                    onValueChange = { model = it },
                    label = { Text("Model") }
                )

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = serial,
                    onValueChange = { serial = it },
                    label = { Text("Serial Number") }
                )

                // Dropdown for Repair Type Selection
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedType.displayName,
                        onValueChange = {},
                        label = { Text("Repair Type") },
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                            .menuAnchor()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        RepairType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedType = type
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = costumerName,
                    onValueChange = { costumerName = it },
                    label = { Text("Customer Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )

                OutlinedTextField(
                    value = technicianName,
                    onValueChange = { technicianName = it },
                    label = { Text("Technician Name") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )

                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // ✅ Modal Bottom Sheet (Only appears when `showBottomSheet` is true)
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Select a Brand",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Sample brands (Replace with real data)
                    val brands by db.phoneBrandsDAO().getAllNames().collectAsState(initial = emptyList())

                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        columns = GridCells.Fixed(3)

                    ) {
                        items(brands) { selectedBrand ->
                            Text(
                                text = selectedBrand,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        brand = selectedBrand // Updates selected brand
                                        showBottomSheet = false // Closes modal
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }

                }
            }
        }

        // ✅ Floating Action Button to save repair
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = {
                // Error handling
                if (model.isEmpty() || serial.isEmpty() || costumerName.isEmpty()) {
                    Toast.makeText(context, "Please fill out the form!", Toast.LENGTH_SHORT).show()
                } else {
                    coroutineScope.launch {
                        val repair = Repair(
                            id = id.toIntOrNull() ?: 0,
                            model = model,
                            serial = serial,
                            type = selectedType.name,
                            costumerName = costumerName,
                            technicianName = technicianName,
                            price = price.toDoubleOrNull() ?: 0.0,
                            repairType = selectedType,
                        )
                        db.repairDAO().insert(repair)
                    }
                    navController.navigate(Destination.RepairDetails.route) // Navigate after saving
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Save new repair")
        }
    }
}
