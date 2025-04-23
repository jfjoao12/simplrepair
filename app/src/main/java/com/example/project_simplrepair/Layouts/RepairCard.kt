package com.example.project_simplrepair.Layouts

import android.annotation.SuppressLint
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.Settings.Global
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.showRepairID
import com.example.project_simplrepair.Operations.taxesCalculation
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalSharedTransitionApi::class, DelicateCoroutinesApi::class)
@Composable
fun RepairCard(
    navController: NavHostController,
    repairItem: Repair,
    customerItem: Customer,
    deviceItem: Device,
    db: AppDatabase,
    animatedVisibilityScope: AnimatedVisibilityScope,
    sharedTransitionScope: SharedTransitionScope,
    onBackPressed: () -> Unit
) {



    with(sharedTransitionScope) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            onClick = {
                navController.navigate("repair_details/${repairItem.id}")
            }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Name and Repair Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Customer name and repair number
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = customerItem.customerName,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState(key = "customerName-${repairItem.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        )
                        val repairIdName = Text(showRepairID(repairItem.id)).toString()
                        Text(
                            text = repairIdName,
                            modifier = Modifier
                                    .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState("repairId-${repairItem.id}"),
                            animatedVisibilityScope = animatedVisibilityScope,
                            ),
                            fontStyle = FontStyle.Italic
                        )
                    }

                    // Technician name and repair status
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = deviceItem.deviceType.toString(),
                            modifier = Modifier.sharedElement(
                                sharedTransitionScope.rememberSharedContentState(key = "techName-${repairItem.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                        )
                        Text("In Repair", fontStyle = FontStyle.Italic)
                    }
                }

                // Box with repair type and phone model
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp) // Space from Row
                        .align(Alignment.CenterHorizontally), // Centered horizontally
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {


                        Text(
                            text = repairItem.repairType.displayName,
                            fontSize = 24.sp,
                            modifier = Modifier
                        )
                        Text(
                            text = "FIX THIS", fontSize = 24.sp,
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState("modelName-${repairItem.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                )
                        )
                    }
                }

                // Price
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "$${taxesCalculation(repairItem.price)}",

                    )
                }
            }
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
fun fetchCustomerDetails(database: AppDatabase, repairId: Int?): Customer? {
    var customer: Customer? = null

    GlobalScope.launch{
        customer = database.repairDAO().getCustomerByRepairId(repairId!!)
    }
    return customer
}

@OptIn(DelicateCoroutinesApi::class)
fun fetchTechnicianDetails(database: AppDatabase, repairId: Int?): Technician? {
    var technician: Technician? = null
    GlobalScope.launch {
        technician = database.repairDAO().getTechByRepairId(repairId!!)
    }
    return technician
}

@OptIn(DelicateCoroutinesApi::class)
fun fetchDeviceDetails(database: AppDatabase, repairId: Int?): Device? {
    var device: Device? = null
    GlobalScope.launch {
        device = database.repairDAO().getDeviceByRepairId(repairId!!)
    }
    return device
}
