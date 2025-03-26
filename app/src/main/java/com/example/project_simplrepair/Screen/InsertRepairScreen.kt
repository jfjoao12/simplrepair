package com.example.project_simplrepair.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.KeyboardType
import kotlinx.coroutines.launch
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Operations.RepairType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertRepairScreen(db: AppDatabase) {
    var id by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var costumerName by remember { mutableStateOf("") }
    var technicianName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(RepairType.BATTERY) } // Default selection

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = id,
            onValueChange = { id = it },
            label = { Text("Repair ID") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = model,
            onValueChange = { model = it },
            label = { Text("Phone Model") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = serial,
            onValueChange = { serial = it },
            label = { Text("Serial Number") },
            modifier = Modifier.fillMaxWidth()
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
                    .menuAnchor() // Ensures proper anchoring for the dropdown
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
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = technicianName,
            onValueChange = { technicianName = it },
            label = { Text("Technician Name") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Price") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                coroutineScope.launch {
                    val repair = Repair(
                        id = id.toIntOrNull() ?: 0,
                        model = model,
                        serial = serial,
                        type = selectedType.name,  // Store enum name instead of text
                        costumerName = costumerName,
                        technicianName = technicianName,
                        price = price.toDoubleOrNull() ?: 0.0,
                        repairType = selectedType
                    )
                    db.repairDAO().insert(repair)
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Repair")
        }
    }
}
