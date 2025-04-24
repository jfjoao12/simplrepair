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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Models.*
import com.example.project_simplrepair.Operations.showRepairID
import com.example.project_simplrepair.Operations.taxesCalculation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalSharedTransitionApi::class, DelicateCoroutinesApi::class)
@Composable
        /**
         * Displays a single repair card in the list.
         *
         * Fetches and shows:
         *  1. Customer name
         *  2. Repair ID
         *  3. Technician name
         *  4. Repair type
         *  5. Device model name
         *  6. Total price (with taxes)
         *
         * While loading the model name and technician name off the IO thread, a spinner
         * is shown in place of the content.
         *
         * @param navController          Nav controller for navigating to details
         * @param repairItem             The Repair record to display
         * @param customerItem           The associated Customer record
         * @param deviceItem             The associated Device record
         * @param db                     AppDatabase used to look up human-readable names
         * @param animatedVisibilityScope Scope for shared element animations
         * @param sharedTransitionScope   Scope for shared element animations
         * @param onBackPressed           Callback invoked on back press from details
         */
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
    // 1) state for the model name and for the tech name
    var modelName by remember { mutableStateOf<String?>(null) }
    var technicianName by remember { mutableStateOf<String?>(null) }

    // 2) once (when either ID changes), load both
    LaunchedEffect(deviceItem.deviceId, repairItem.technicianId) {
        GlobalScope.launch {
            // fetch the human-readable phone model
            modelName = deviceItem.deviceId
                ?.let { db.deviceDao().getModelNameByDeviceId(it) }
                ?: "Unknown model"

            // fetch the tech’s name
            technicianName = repairItem.technicianId
                ?.let { db.technicianDao().getTechNameById(it) }
                ?: "Unknown technician"
        }
    }

    // 3) spinner until both arrive
    if (modelName == null || technicianName == null) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
                .semantics {
                    contentDescription = "Loading repair card details"
                },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.semantics {
                    contentDescription = "Loading"
                }
            )
        }
        return
    }

    with(sharedTransitionScope) {
        ElevatedCard(
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .semantics {
                    contentDescription = "Repair for ${customerItem.customerName}, ID ${showRepairID(repairItem.id)}"
                    heading()
                },
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
                        modifier = Modifier
                            .weight(1f)
                            .semantics { heading() },
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(
                            text = customerItem.customerName,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.semantics {
                                contentDescription = "Customer: ${customerItem.customerName}"
                            }
                        )
                        Text(
                            text = showRepairID(repairItem.id),
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.semantics {
                                contentDescription = "Repair ID: ${showRepairID(repairItem.id)}"
                            }
                        )
                    }

                    // Technician name and repair status
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .semantics { heading() },
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = technicianName!!,
                            modifier = Modifier.semantics {
                                contentDescription = "Technician: $technicianName"
                            }
                        )
                        Text(
                            "In Repair",
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.semantics {
                                contentDescription = "Status: In Repair"
                            }
                        )
                    }
                }

                // Box with repair type and phone model
                Box(
                    modifier = Modifier
                        .padding(top = 20.dp, bottom = 20.dp)
                        .align(Alignment.CenterHorizontally)
                        .semantics { heading() },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = repairItem.repairType.displayName,
                            fontSize = 24.sp,
                            modifier = Modifier.semantics {
                                contentDescription = "Repair Type: ${repairItem.repairType.displayName}"
                            }
                        )
                        Text(
                            text = modelName!!,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .semantics {
                                    contentDescription = "Device Model: $modelName"
                                }
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState("modelName-${repairItem.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope
                                )
                        )
                    }
                }

                // Price
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally)
                        .semantics {
                            contentDescription = "Total Price: $${taxesCalculation(repairItem.price)}"
                        },
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(text = "$${taxesCalculation(repairItem.price)}")
                }
            }
        }
    }
}
