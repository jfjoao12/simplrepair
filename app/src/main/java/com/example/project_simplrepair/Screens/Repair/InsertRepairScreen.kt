package com.example.project_simplrepair.Screens.Repair

import android.Manifest
import android.graphics.BitmapFactory.decodeFile
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.CustomCardLayout
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Operations.DeviceType
import com.example.project_simplrepair.Operations.RepairType
import com.example.project_simplrepair.ViewModels.InsertRepairViewModel
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
 * @param appDatabase            the [AppDatabase] used to insert both Device and Repair.
 * @param navController used to navigate back to the main Repairs list.
 * @param photoPaths    lit of the paths of the photos taken.
 * @param insertVm      used to persist data in the form when screen is changed to camera back and forth.
 * @param repairItem    the Repair object fetched from the database.
 */
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
    ExperimentalPermissionsApi::class
)
@Composable
fun InsertRepairScreen(
    paddingValues: PaddingValues,
    appDatabase: AppDatabase,
    navController: NavController,
    photoPaths: List<String>,
    insertVm: InsertRepairViewModel,
    repairItem: Repair? = null
) {
    var customer by remember { mutableStateOf<Customer?>(null) }
    var device by remember { mutableStateOf<Device?>(null) }
    var deviceModel by remember { mutableStateOf<String?>(null) }
    val devices by appDatabase.phoneModelsDAO().getModelByName(insertVm.modelName).collectAsState(initial = emptyList())

    if (repairItem != null){
        LaunchedEffect(repairItem.id) {
            GlobalScope.launch {
                // these DAO calls run off the main thread
                val c = repairItem.id?.let { appDatabase.customerDao().getCustomerByRepairId(it) }
                val d = repairItem.id?.let { appDatabase.deviceDao().getDeviceByRepairId(it) }
                val dm = appDatabase.deviceDao().getModelNameByDeviceId(d!!.deviceId!!)
                // now post them back to Compose state
                customer = c
                device = d
                deviceModel = dm
            }
        }
        insertVm.serial = device!!.deviceSerial
        insertVm.modelName = deviceModel.toString()
        insertVm.customerName = customer!!.customerName
        insertVm.customerId = customer!!.customerId!!
        insertVm.price = repairItem.price.toString()
        insertVm.selectedType = repairItem.repairType
        insertVm.notes = repairItem.notes
    }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Bottom sheet states
    var expanded by  remember { mutableStateOf(false) }
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
                    .padding(bottom = 16.dp)
                    .semantics { heading() }
            ) {
                ScreenTitle("New repair")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                // Photo previews
                if (photoPaths.isNotEmpty()) {
                    item {
                        CustomCardLayout("Device Photo") {
                            LazyRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {
                                items(photoPaths) { path ->
                                    val bmp = remember(path) { decodeFile(path) }
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
                    }
                }

                // Device fields
                item {
                    CustomCardLayout("Device") {
                        OutlinedTextField(
                            value = insertVm.modelName,
                            onValueChange = { insertVm.modelName = it },
                            label = { Text(insertVm.modelBrand) },
                            singleLine = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    GlobalScope.launch {
                                        val models = appDatabase.phoneModelsDAO().getModelByName(insertVm.modelName).first()
                                        if (models.size == 1) {
                                            insertVm.phoneSpecs = models.first()
                                        } else {
                                            deviceBottomModalSheet = true
                                        }
                                    }
                                }) {
                                    Icon(Icons.Default.Search, contentDescription = "Search models")
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = insertVm.serial,
                            onValueChange = { insertVm.serial = it },
                            singleLine = true,
                            label = { Text("Serial Number / IMEI") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .semantics { contentDescription = "Device serial input" }
                        )
                    }
                }

                // Customer fields
                item {
                    CustomCardLayout("Customer") {
                        OutlinedTextField(
                            value = insertVm.customerName,
                            onValueChange = { insertVm.customerName = it },
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
                item {
                    RepairInfoSection(
                        insertVm
                    )
                }

                // Repair type, price and notes
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
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(devices) { model ->
                            Text(
                                text = model.name,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        insertVm.phoneSpecs = model
                                        insertVm.modelName = model.name
                                        insertVm.modelBrand = model.brand?.name.toString()
                                        insertVm.phoneSpecsId = model.id
                                        deviceBottomModalSheet = false
                                    }
                                    .padding(8.dp)
                                    .semantics {
                                        contentDescription = "Device option ${model.name}"
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
                    val customerList by appDatabase.customerDao().getCustomerByName(insertVm.customerName).collectAsState(initial = emptyList())
                    LazyVerticalGrid(columns = GridCells.Fixed(1), modifier = Modifier.fillMaxWidth()) {
                        items(customerList) { cust ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        insertVm.customerId = cust.customerId!!
                                        insertVm.customerName = cust.customerName
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
                    if (insertVm.serial.isBlank() || insertVm.customerId == 0) {
                        Toast.makeText(context, "Please fill in serial & select customer", Toast.LENGTH_SHORT).show()
                        return@FloatingActionButton
                    }
                    coroutineScope.launch {
                        // Insert Device then Repair, then update photo with repairId
                        val newDeviceId = withContext(Dispatchers.IO) {
                            val dev = deviceModel?.let {
                                Device(
                                    null,
                                    insertVm.phoneSpecsId,
                                    insertVm.customerId,
                                    insertVm.phoneSpecs?.id ?: 0,
                                    DeviceType.MOBILE,
                                    insertVm.serial,
                                    it,

                                )
                            }
                            appDatabase.deviceDao().insert(dev!!)
                        }.toInt()


                        val newRepair = withContext(Dispatchers.IO) {
                            val rep = Repair(
                                null,
                                insertVm.customerId,
                                newDeviceId, 1,
                                insertVm.price.toDoubleOrNull() ?: 0.0,
                                insertVm.notes,
                                repairType = insertVm.selectedType
                            )
                            appDatabase.repairDAO().insert(rep)
                        }

                      withContext(Dispatchers.IO) {
                            photoPaths.forEach{ path ->
                                var devicePhoto =
                                    DevicePhoto(
                                        photoId = null,
                                        repairId = newRepair.toInt(),
                                        filePath = path,
                                    )
                                appDatabase.devicePhotoDao().updatePhotoRepairId(newRepair.toInt(), path)
                            }
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
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairInfoSection (
    insertVm: InsertRepairViewModel,
    modifier: Modifier? = null
) {

    var expanded by remember { mutableStateOf(false) }
    CustomCardLayout("Repair Info") {
        OutlinedTextField(
            value = insertVm.notes,
            onValueChange = { insertVm.notes = it },
            label = { Text("Notes") },
            minLines = 2,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .semantics { contentDescription = "Repair Notes Input" }
        )
        // Repair Type dropdown
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = insertVm.selectedType.displayName,
                onValueChange = {},
                label = { Text("Repair Type") },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .menuAnchor(MenuAnchorType.PrimaryEditable)

            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                RepairType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.displayName) },
                        onClick = {
                            insertVm.selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = insertVm.price,
            onValueChange = { insertVm.price = it },
            label = { Text("Price") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .semantics { contentDescription = "Repair price input" }
        )
    }
}

