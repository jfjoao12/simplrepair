package com.example.project_simplrepair.Screens

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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
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

/**
 * Displays a scrollable list of repairs.
 * Each repair card is clickable to view details.
 * Provides a FAB to add a new repair.
 *
 * @param paddingValues Window insets to apply as padding.
 * @param navController NavController for navigation actions.
 * @param db        AppDatabase instance to load repairs.
 * @param sharedTransitionScope Scope for shared-element transitions.
 * @param animatedContentScope   Scope for animated content transitions.
 * @param onItemClick Callback invoked with the repair ID when a card is clicked.
 */
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

    // 2) Enrich each repair with its related customer, device, and technician
    val enrichedRepairs by produceState(initialValue = emptyList<EnrichedRepair>(), repairs) {
        value = repairs.map { r ->
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
            .semantics { contentDescription = "Repairs screen" }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = paddingValues.calculateBottomPadding())
                .semantics { contentDescription = "List of repairs" },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mark the title as a heading for accessibility
            Box(modifier = Modifier.semantics { heading() }) {
                ScreenTitle("Repairs")
            }

            if (enrichedRepairs.isEmpty()) {
                Text(
                    "No repairs found.",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .semantics { contentDescription = "No repairs found" }
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
