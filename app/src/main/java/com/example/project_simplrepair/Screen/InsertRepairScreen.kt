package com.example.project_simplrepair.Screen

import android.Manifest
import android.graphics.BitmapFactory
import android.graphics.BitmapFactory.decodeFile
import android.provider.ContactsContract.Contacts.Photo
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project_simplrepair.Camera.PhotoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.PhoneModels
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.DeviceType
import com.example.project_simplrepair.Operations.RepairType
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


/**
 * A screen for creating a new [Repair].
 *
 * Presents sections for selecting/entering the device, customer,
 * repair type, and price, then saves both the new [Device] and [Repair]
 * records in the database and returns to the main view.
 *
 * @param paddingValues insets from the Scaffold (status & nav bars).
 * @param db            the [AppDatabase] used to insert both Device and Repair.
 * @param navController used to navigate back to the main Repairs list.
 */
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun InsertRepairScreen(
    paddingValues: PaddingValues,
    db: AppDatabase,
    navController: NavController,
    photoPaths: List<String>,
) {
    var modelName by remember { mutableStateOf("") }
    var serial by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerId by remember { mutableIntStateOf(0) }
    var phoneModel by remember { mutableStateOf<PhoneModels?>(null) }
    var price by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedType by remember { mutableStateOf(RepairType.BATTERY) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Bottom sheet states
    val brandSheetState = rememberModalBottomSheetState()
    var brandBottomModalSheet by remember { mutableStateOf(false) }
    val customerSheetState = rememberModalBottomSheetState()
    var customerBottomModalSheet by remember { mutableStateOf(false) }
    val deviceSheetState = rememberModalBottomSheetState()
    var deviceBottomModalSheet by remember { mutableStateOf(false) }

    // Camera Stuff
    //var showCamera by remember { mutableStateOf(false) }
    //var photoPaths by remember { mutableStateOf<List<String>>(emptyList()) }
//    val newPhotoPaths = remember { mutableStateListOf<String>() }


    val cameraPermissionState = rememberPermissionState(
        Manifest.permission.CAMERA
    )



//    GlobalScope.launch {
//        photoPaths = db.devicePhotoDao().getPhotosPath()
//    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

//    if (showCamera) {
//        CameraScreen(
//            onPhotoTaken = { path ->
//                // immediately update the preview list
//                newPhotoPaths += path
//                showCamera = false
//            },
//            onCancel = {
//                showCamera = false
//            }
//        )
//    } else {
//
//    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {

                ScreenTitle(
                    title = "New Repair",
                    modifier = Modifier
                        .semantics { heading() }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                // Device photos section
                if (photoPaths.isNotEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            ElevatedCard(
                                shape = RoundedCornerShape(12.dp),
                                elevation = CardDefaults.cardElevation(4.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                LazyRow(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = 16.dp,
                                            top = 32.dp,
                                            bottom = 32.dp,
                                            end = 16.dp
                                        )
                                ) {
                                    items(photoPaths) { path ->
                                        val bmp = remember(path) {
                                            decodeFile(path)
                                        }
                                        bmp?.let {
                                            Image(
                                                bitmap = it.asImageBitmap(),
                                                contentDescription = "New photo preview",
                                                modifier = Modifier
                                                    .size(128.dp)
                                                    .padding(2.dp)
                                                    .rotate(90f)
                                            )
                                        }
                                    }
                                }
                            }

                            Surface(
                                color = MaterialTheme.colorScheme.secondaryContainer,
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier
                                    .align(Alignment.TopCenter)
                                    .offset(y = (-12).dp)
                                    .zIndex(1f)
                            ) {
                                Text(
                                    text = "Device Picture",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Device section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        ElevatedCard(
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 28.dp,
                                        start = 16.dp,
                                        end = 16.dp,
                                        bottom = 16.dp
                                    ),
                            ) {
                                OutlinedTextField(
                                    value = modelName,
                                    onValueChange = {modelName = it},
                                    label = { Text("Model") },
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = {
                                            GlobalScope.launch {
                                                val models = db.phoneModelsDAO().getModelByName(modelName).first()
                                                if (models.size == 1) {
                                                    phoneModel = models.first()
                                                } else {
                                                    deviceBottomModalSheet = true
                                                }
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Search,
                                                contentDescription = "Search models"
                                            )
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                                OutlinedTextField(
                                    value = serial,
                                    onValueChange = { serial = it },
                                    label = { Text("Serial Number / IMEI") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .semantics { contentDescription = "Device serial input" }
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-12).dp)
                                .zIndex(1f)
                        ) {
                            Text(
                                text = "Phone",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Customer section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        ElevatedCard(
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 28.dp, start = 16.dp, end = 8.dp, bottom = 16.dp),
                            ) {
                                OutlinedTextField(
                                    value = customerName,
                                    onValueChange = { customerName = it },
                                    label = { Text("Name") },
                                    singleLine = true,
                                    trailingIcon = {
                                        IconButton(onClick = { customerBottomModalSheet = true }) {
                                            Icon(Icons.Default.Search, contentDescription = "Search for customer")
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }

                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-12).dp)
                                .zIndex(1f)
                        ) {
                            Text(
                                text = "Customer",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // Repair info section
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ){
                        ElevatedCard(
                            shape = RoundedCornerShape(12.dp),
                            elevation = CardDefaults.cardElevation(4.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 28.dp, start = 16.dp, end = 8.dp, bottom = 16.dp),
                            ) {
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = it },
                                    modifier = Modifier.semantics { contentDescription = "Repair type dropdown" }
                                ) {
                                    OutlinedTextField(
                                        value = selectedType.displayName,
                                        onValueChange = {},
                                        label = { Text("Repair Type") },
                                        readOnly = true,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 8.dp)
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
                                    value = price,
                                    onValueChange = { price = it },
                                    label = { Text("Price") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp)
                                        .semantics { contentDescription = "Repair price input" }
                                )
                            }
                        }
                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .align(Alignment.TopCenter)
                                .offset(y = (-12).dp)
                                .zIndex(1f)
                        ) {
                            Text(
                                text = "Repair Info",
                                style = MaterialTheme.typography.labelLarge,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }

        // Device Bottom Sheet
        if (deviceBottomModalSheet) {
            ModalBottomSheet(
                onDismissRequest = { deviceBottomModalSheet = false },
                sheetState = deviceSheetState
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Select a Device")
                    val devices by db.phoneModelsDAO().getModelByName(modelName).collectAsState(initial = emptyList())
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(devices) { dm ->
                            Text(
                                text = dm.phoneModelName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        phoneModel = dm
                                        deviceBottomModalSheet = false
                                    }
                                    .padding(8.dp)
                                    .semantics {
                                        contentDescription = "Device option ${dm.phoneModelName}"
                                    }
                            )
                        }
                    }
                }
            }
        }

        // Customer Bottom Sheet
        if (customerBottomModalSheet) {
            ModalBottomSheet(
                onDismissRequest = { customerBottomModalSheet = false },
                sheetState = customerSheetState
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Select a Customer")
                    val customerList by db.customerDao().getCustomerByName(customerName).collectAsState(initial = emptyList())
                    LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.fillMaxWidth()) {
                        items(customerList) { cust ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        customerId = cust.customerId!!
                                        customerName = cust.customerName
                                        customerBottomModalSheet = false
                                    }
                                    .padding(8.dp)
                                    .semantics {
                                        contentDescription = "Customer option ${cust.customerName}"
                                    },
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(cust.customerName)
                                Text(cust.customerPhone)
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

            SmallFloatingActionButton(
                onClick = {
                    navController.navigate(Destination.Camera.route)
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .semantics { contentDescription = "Take Photo of Device" }
            ) {
                Icon(Icons.Filled.Camera, contentDescription = "Take Photo Icon")
            }

            FloatingActionButton(
                onClick = {
                    if (serial.isBlank() || customerId == 0) {
                        Toast.makeText(context, "Please fill in serial & select customer", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    coroutineScope.launch {
                        // Insert Device then Repair
                        val newDeviceId = withContext(Dispatchers.IO) {
                            val dev = Device(null, customerId, phoneModel?.id ?: 0, DeviceType.MOBILE, serial)
                            db.deviceDao().insert(dev)
                        }.toInt()

                        withContext(Dispatchers.IO) {
                            val rep = Repair(null, customerId, newDeviceId, 1, price.toDoubleOrNull() ?: 0.0, "", selectedType)
                            db.repairDAO().insert(rep)
                        }
                        navController.navigate(Destination.Main.route)
                    }
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .semantics { contentDescription = "Save new repair" }
            ) {
                Icon(Icons.Filled.Add, contentDescription = null)
            }
        }
        // Save button

    }
}
