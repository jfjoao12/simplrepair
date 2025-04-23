package com.example.project_simplrepair.Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.PhoneModels
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.DeviceType
import com.example.project_simplrepair.Operations.RepairType
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class)
@Composable
fun InsertRepairScreen(paddingValues: PaddingValues, db: AppDatabase, navController: NavController) {
    var id by remember { mutableStateOf("") }
    var modelName by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerId by remember { mutableIntStateOf(0) }
    var deviceId by remember { mutableIntStateOf(0) }

    var brand by remember { mutableStateOf("") }
    var phoneModel by remember { mutableStateOf<PhoneModels?>(null) }
    // When Device is selected, the outlinetextfield placeholder will change to the
    // brand of the device
    var modelTextFieldLabel by remember { mutableStateOf("Model") }

    var device by remember { mutableStateOf<Device?>(null) }
    var newRepair by remember { mutableStateOf<Repair?>(null) }
    var technicianName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(RepairType.BATTERY) } // Default selection

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Brand Selection Bottom Sheet
    val brandSheetState = rememberModalBottomSheetState()
    var brandBottomModalSheet by remember { mutableStateOf(false) }

    // Customer Selection Bottom Sheet
    val customerSheetState = rememberModalBottomSheetState()
    var customerBottomModalSheet by remember { mutableStateOf(false) }

    // Device Selection Bottom Sheet
    val deviceSheetState = rememberModalBottomSheetState()
    var deviceBottomModalSheet by remember { mutableStateOf(false) }



    // DocStrings
    /**
     * Add documentation!
     */

    // Look into shared intent
    // Charting
    // Attach image of before
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
    ) {
        Column {
            ScreenTitle("New Repair")
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                // Create device section
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 0.dp),
                        ) {
                            Text(
                                text = "Device",
                                modifier = Modifier.padding(10.dp)
                            )
//                            OutlinedTextField(
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(5.dp)
//                                    .clickable { brandBottomModalSheet = true },
//                                value = brand,
//                                onValueChange = {},
//                                label = { Text("Brand") },
//                                readOnly = true,
//                                enabled = false,
//                                colors = OutlinedTextFieldDefaults.colors(
//                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
//                                    disabledBorderColor = MaterialTheme.colorScheme.outline,
//                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
//                                    disabledContainerColor = MaterialTheme.colorScheme.surface
//                                )
//                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                value = modelName,
                                onValueChange = { modelName = it },
                                label = {
                                    Text(
                                        modelTextFieldLabel
                                    )
                                }
                            )


                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                value = serial,
                                onValueChange = { serial = it },
                                label = { Text("Serial Number / IMEI") }
                            )
                            Button(
                                onClick = {
                                    GlobalScope.launch {
                                        var modelSearch = db.phoneModelsDAO().getModelByName(modelName).first()
                                        val searchedModelBrandId = modelSearch.first().brandId
                                        val brandName = db.phoneModelsDAO().getBrandNameById(searchedModelBrandId)

                                        if (modelSearch.size == 1) {

                                            brand = brandName
                                            modelTextFieldLabel = brandName
                                            Log.i("onClick if", "${modelSearch.count()}")

                                        } else {
                                            deviceBottomModalSheet = true

                                            Log.i("onClick else", "${modelSearch.count()}")

                                        }
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(16.dp)
                            ) {
                                Text ("Search Device")
                            }
                        }
                    }
                }

                // Customer selection section
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Customer",
                                modifier = Modifier.padding(10.dp)
                            )
                            OutlinedTextField(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(5.dp),
                                value = customerName,
                                onValueChange = { customerName = it },
                                label = { Text("Name") }
                            )
                            Button(
                                onClick = {
                                    customerBottomModalSheet = true
                                },
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(16.dp)
                            ) {
                                Text ("Search Customer")
                            }
                        }
                    }
                }
                // Repair info section
                item {
                    ElevatedCard(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    )  {
                        Column(
                            modifier = Modifier
                                .padding(20.dp)
                        ) {
                            Text(
                                text = "Repair Info",
                                modifier = Modifier.padding(10.dp)
                            )
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
                                            //(MenuAnchorType.PrimaryEditable, true)
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
//                            OutlinedTextField(
//                                value = technicianName,
//                                onValueChange = { technicianName = it },
//                                label = { Text("Technician Name") },
//                                modifier = Modifier
//                                    .fillMaxWidth()
//                                    .padding(5.dp)
//                            )
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
                }
            }
        }


        // Brand Bottom Modal Sheet
        if (brandBottomModalSheet) {
            ModalBottomSheet(
                onDismissRequest = { brandBottomModalSheet = false },
                sheetState = brandSheetState,
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
                                        brandBottomModalSheet = false // Closes modal
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Device Bottom Modal Sheet
        if (deviceBottomModalSheet) {
            ModalBottomSheet(
                onDismissRequest = { deviceBottomModalSheet = false },
                sheetState = deviceSheetState,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Select a Device",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    val devices by db.phoneModelsDAO().getModelByName(modelName).collectAsState(initial = emptyList())
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .align(Alignment.CenterHorizontally),
                        columns = GridCells.Fixed(1)
                    ) {
                        items(devices) { selectedDeviceModel ->
                            Text(
                                text = selectedDeviceModel.phoneModelName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        phoneModel = selectedDeviceModel
                                        modelName = selectedDeviceModel.phoneModelName
                                        deviceBottomModalSheet = false
                                    }
                                    .padding(8.dp)
                            )
                        }
                    }
                }
            }
        }

        // Customer Selection Bottom Modal Sheet
        if (customerBottomModalSheet) {
            ModalBottomSheet(
                onDismissRequest = { customerBottomModalSheet = false },
                sheetState = customerSheetState,
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Select a Customer",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    val customerList by db.customerDao().getCustomerByName(customerName).collectAsState(initial = emptyList())
                    LazyVerticalGrid(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        columns = GridCells.Fixed(1)
                    ) {
                        items(customerList) { selectedCustomer ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        customerName =
                                            selectedCustomer.customerName // Updates selected customer
                                        customerId = selectedCustomer.customerId!!
                                        customerBottomModalSheet = false // Closes modal
                                        Log.i("customerId", "CustomerID is $customerId")
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = selectedCustomer.customerName,
                                    modifier = Modifier.padding(8.dp)
                                )
                                Text(
                                    text = selectedCustomer.customerPhone,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = {
                if (serial.isBlank() || customerId == 0) {
                    Toast.makeText(context, "Please fill in serial & select customer", Toast.LENGTH_SHORT).show()
                    return@FloatingActionButton
                }

                coroutineScope.launch {
                    // 1️⃣ Insert Device on IO, grab its new primary key:
                    val newDeviceId = withContext(Dispatchers.IO) {
                        val newDevice = Device(
                            deviceId     = null,
                            customerId   = customerId,
                            phoneModelId = phoneModel?.id ?: 0,
                            deviceType   = DeviceType.MOBILE,
                            deviceSerial = serial
                        )
                        db.deviceDao().insert(newDevice)
                    }.toInt()

                    withContext(Dispatchers.IO) {
                        val newRepair = Repair(
                            id           = null,
                            customerId   = customerId,
                            deviceId     = newDeviceId,
                            technicianId = 1,
                            price        = price.toDoubleOrNull() ?: 0.0,
                            notes        = "",
                            repairType   = selectedType
                        )
                        db.repairDAO().insert(newRepair)
                    }

                    navController.navigate(Destination.Main.route)
                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor   = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Save new repair")
        }
    }
}
