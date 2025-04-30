package com.example.project_simplrepair.Screens

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Layouts.CustomCardLayout
import com.example.project_simplrepair.Layouts.InfoCard
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Inventory
import com.example.project_simplrepair.Models.Invoice
import com.example.project_simplrepair.Models.PaymentMethods
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.RepairStatus
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.RepairType
import com.example.project_simplrepair.Operations.taxesCalculation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class, DelicateCoroutinesApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun InvoiceScreen(
    modifier: Modifier,
    repairItem: Repair,
    paddingValues: PaddingValues,
    navController: NavController,
    db: AppDatabase
) {
    var searchParam by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Inventory>>(emptyList()) }
    var selectedPaymentType by remember { mutableStateOf(PaymentMethods.DEBIT) }
    var partsModal by remember { mutableStateOf(false) }
    var paymentReference by remember { mutableStateOf("") }
    var subTotal by remember { mutableDoubleStateOf(0.0) }
    var total by remember { mutableDoubleStateOf(0.0) }

    val coroutineScope = rememberCoroutineScope()
    // Parts selection state
    var partQuery by remember { mutableStateOf("") }
    val selectedParts = remember { mutableStateListOf<Inventory>() }

    val partsSheetState = rememberModalBottomSheetState()
    val inventoryPartsQuantity by remember { mutableIntStateOf(0)}



    // state holders for the related entities
    var customer by remember { mutableStateOf<Customer?>(null) }


    // Kick off a one-time load when repairItem.id changes
    LaunchedEffect(repairItem.id) {
        GlobalScope.launch {
            // these DAO calls run off the main thread
            val c = repairItem.id?.let { db.repairDAO().getCustomerByRepairId(it) }
            customer = c

        }
    }

    if (customer == null) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .semantics { contentDescription = "Invoice Screen" }
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {

                ScreenTitle("Invoice")
            }

            item {

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)

                ) {
                    InfoCard(
                        title    = "Customer",
                        item     = customer,
                        visible  = {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = customer!!.customerName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Text(
                                    text = customer!!.customerPhone,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        },
                        expanded = {
                            Row (
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                customer!!.customerEmail?.let { email ->
                                    Text(
                                        text = "Email: ",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    Text(
                                        text = email,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Address: ",
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text(
                                    text = customer!!.customerAddress.toString(),
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("")
                                Text(
                                    text = "${customer!!.customerCity}, ${customer!!.customerProv} ${customer!!.customerPostalCode}",
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )
                }
            }

            item {
                CustomCardLayout ("Amount Due") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal: ")
                        Text(
                            text = "$subTotal",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total: ")
                        Text(
                            text = "$total",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }


            item {
                CustomCardLayout("Repair Items") {
                    OutlinedTextField(
                        value = partQuery,
                        onValueChange = { partQuery = it },
                        label = { Text("Search Parts") },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    coroutineScope.launch {
                                        partsModal = true
                                    }                                    }
                            ) {
                                Icon(Icons.Default.Search, contentDescription = "Search for Part")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Column {
                        if (selectedParts.isNotEmpty()) {
                            selectedParts.forEach { part ->
                                var expanded by remember(part) { mutableStateOf(false) }
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                        .clickable { expanded = !expanded },
                                ) {
                                    Column {
                                        Row(
                                            modifier =
                                            Modifier
                                                .fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {

                                            Text(
                                                text = part.name,
                                                style = MaterialTheme.typography.bodyMedium,
                                                textAlign = TextAlign.Start
                                            )
                                            Row(
                                                horizontalArrangement = Arrangement.SpaceAround,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = part.price.toString(),
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    textAlign = TextAlign.End
                                                )

                                                AnimatedVisibility(visible = expanded) {
                                                    IconButton(
                                                        modifier = Modifier
                                                            .size(32.dp)
                                                            .padding(start = 6.dp),
                                                        onClick = {
                                                            selectedParts -= part
                                                            subTotal -= part.price
                                                            total = taxesCalculation(subTotal)
                                                        }
                                                    ) {
                                                        Icon(
                                                            imageVector = Icons.Filled.Delete,
                                                            contentDescription = "Delete Icon"
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }



            item {

                CustomCardLayout("Payment") {
                    var paymentExpanded by remember { mutableStateOf(false) }

                    Column {

                        ExposedDropdownMenuBox(
                            expanded = paymentExpanded,
                            onExpandedChange = { paymentExpanded = !paymentExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedPaymentType.displayName,
                                onValueChange = {},
                                label = { Text("Payment Type") },
                                readOnly = true,
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(paymentExpanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .menuAnchor(MenuAnchorType.PrimaryEditable)

                            )
                            ExposedDropdownMenu(
                                expanded = paymentExpanded,
                                onDismissRequest = { paymentExpanded = false }
                            ) {
                                PaymentMethods.entries.forEach { type ->
                                    DropdownMenuItem(
                                        text = { Text(type.displayName) },
                                        onClick = {
                                            selectedPaymentType = type
                                            paymentExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = paymentReference,
                            onValueChange = { paymentReference = it },
                            label = { Text("Payment Reference") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .semantics { contentDescription = "Payment Reference" }
                        )
                    }
                }
            }

        }





        // Bottom sheet for part selection
        if (partsModal) {
            ModalBottomSheet(
                onDismissRequest = { partsModal = false },
                sheetState = partsSheetState
            ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .align(Alignment.CenterHorizontally)
                    ) {
                        Text(
                            "Part Search Result",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }


                    LazyColumn (
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        GlobalScope.launch {
                            searchResults = db.inventoryDao().search(partQuery)

                        }

                        items(searchResults) { part ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        subTotal += part.price
                                        total = taxesCalculation(subTotal)
                                        selectedParts += part
                                        partsModal = false
                                    }
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Text(part.quantity.toString())
                                    Text(
                                        selectedParts.count().toString(),
                                        style = TextStyle(fontWeight = FontWeight.Thin)
                                    )
                                }
                                Text(part.name)
                                Text("$${part.price}", textAlign = TextAlign.End)
                            }
                            HorizontalDivider()
                        }
                    }

            }
        }

        // Floating action to create/save invoice
        FloatingActionButton(
            onClick = {
                coroutineScope.launch {

                    val newInvoice = Invoice(
                        id           = null,
                        repairId     = repairItem.id ?: 0,
                        customerId   = repairItem.customerId ?: 0,
                        paymentMethod= selectedPaymentType,      // your PaymentMethods enum
                        string       = paymentReference,         // the reference string you collected
                        subTotal     = subTotal,
                        total        = total
                    )

                    db.invoiceDao().insert(newInvoice)

                    db.repairDAO().closeRepairByid(repairItem.id ?: 0, RepairStatus.COMPLETED)

                    navController.popBackStack()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp)
                .semantics { contentDescription = "Save Invoice" },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(Icons.Filled.Check, contentDescription = "Add")
        }

    }

}