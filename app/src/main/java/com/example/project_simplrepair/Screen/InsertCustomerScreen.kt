package com.example.project_simplrepair.Screen

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Models.Customer
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun InsertCustomerScreen(paddingValues: PaddingValues, db: AppDatabase, navController: NavController) {
    var customerName by remember { mutableStateOf("") }
    var customerEmail by remember { mutableStateOf("") }
    var customerCity by remember { mutableStateOf("Winnipeg") }
    var customerCountry by remember { mutableStateOf("Canada") }
    var customerPostalCode by remember { mutableStateOf("") }
    var customerProv by remember { mutableStateOf("MB") }
    var customerAddress by remember { mutableStateOf("") }
    var customerAddressTwo by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    var customerPhoneTwo by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
        ) {
            item {
                Text(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    text = "New Customer"
                )
            }

            val fields = listOf(
                "Name" to customerName,
                "Email" to customerEmail,
                "City" to customerCity,
                "Country" to customerCountry,
                "Postal Code" to customerPostalCode,
                "Province" to customerProv,
                "Address" to customerAddress,
                "Address 2" to customerAddressTwo,
                "Phone" to customerPhone,
                "Phone 2" to customerPhoneTwo
            )

            items(fields) { (label, value) ->
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(5.dp),
                    value = value,
                    onValueChange = {
                        when (label) {
                            "Name" -> customerName = it
                            "Email" -> customerEmail = it
                            "City" -> customerCity = it
                            "Country" -> customerCountry = it
                            "Postal Code" -> customerPostalCode = it
                            "Province" -> customerProv = it
                            "Address" -> customerAddress = it
                            "Address 2" -> customerAddressTwo = it
                            "Phone" -> customerPhone = it
                            "Phone 2" -> customerPhoneTwo = it
                        }
                    },
                    label = { Text(label) }
                )
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = {
                if (customerName.isEmpty() || customerPhone.isEmpty() || customerPostalCode.isEmpty()) {
                    Toast.makeText(context, "Please fill out the form!", Toast.LENGTH_SHORT).show()
                } else {
                    GlobalScope.launch {
                        val customer = Customer(
                            customerId = null,
                            deviceId = null,
                            customerName = customerName,
                            customerEmail = customerEmail,
                            customerCity = customerCity,
                            customerCountry = customerCountry,
                            customerPostalCode = customerPostalCode,
                            customerProv = customerProv,
                            customerAddress = customerAddress,
                            customerAddressTwo = customerAddressTwo,
                            customerPhone = customerPhone,
                            customerPhoneTwo = customerPhoneTwo,
                        )
                        Log.i("Customer", "$customer")
                        db.customerDao().insert(customer)
                    }
                    navController.navigate(Destination.RepairDetails.route)

                }
            },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Save new repair")
        }
    }
}
