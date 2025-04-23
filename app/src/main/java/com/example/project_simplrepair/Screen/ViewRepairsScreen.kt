package com.example.project_simplrepair.Screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.RepairCard
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician

// Simple data holder to keep Repair + its related entities together
private data class EnrichedRepair(
    val repair: Repair,
    val customer: Customer,
    val device: Device,
    val technician: Technician
)

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RepairScreen(
    paddingValues: PaddingValues,
    navController: NavHostController,
    db: AppDatabase,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (Int) -> Unit
) {
    // 1) Load the raw list of repairs
    var repairs by remember { mutableStateOf<List<Repair>>(emptyList()) }
    LaunchedEffect(Unit) {
        repairs = db.repairDAO().getAllRepairs()
    }

    // 2) Produce a list of EnrichedRepair once `repairs` changes
    val enrichedRepairs by produceState(initialValue = emptyList<EnrichedRepair>(), repairs) {
        value = repairs.map { r ->
            // these suspend calls run in this producer coroutine
            val cust = db.repairDAO().getCustomerByRepairId(r.id!!)
            val dev  = db.repairDAO().getDeviceByRepairId(r.id)
            val tech = db.repairDAO().getTechByRepairId(r.id)
            EnrichedRepair(r, cust, dev, tech)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ScreenTitle("Repairs")
            Spacer(Modifier.height(16.dp))

            if (enrichedRepairs.isEmpty()) {
                Text(
                    "No repairs found.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                enrichedRepairs.forEach { (repair, customer, device, technician) ->
                    RepairCard(
                        navController = navController,
                        animatedVisibilityScope = animatedContentScope,
                        sharedTransitionScope = sharedTransitionScope,
                        db = db,
                        onBackPressed = { /* no-op */ },
                        customerItem = customer,
                        deviceItem = device,
                        repairItem = repair
                    )
                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = { navController.navigate(Destination.NewRepair.route) },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add new repair")
        }
    }
}
