package com.example.project_simplrepair.Screens.Repair

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
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
import com.example.project_simplrepair.Models.RepairStatus
import com.example.project_simplrepair.Models.Technician
import kotlinx.coroutines.flow.Flow
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.project_simplrepair.hilt.TicketViewModel


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
    onItemClick: (Int) -> Unit,
    ticketVm: TicketViewModel = hiltViewModel()

) {
    val fullTickets by ticketVm.fullTickets.collectAsState()







    // 1) Load the raw list of repairs
//    var repairs by remember { mutableStateListOf<Flow<List<Repair>>>(emptyList()) }
//    LaunchedEffect(Unit) {
//        repairs = db.repairDAO().getAllRepairs()
//    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .semantics { contentDescription = "Repairs screen" }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .semantics { contentDescription = "List of repairs" },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mark the title as a heading for accessibility
            Box(modifier = Modifier.semantics { heading() }) {
                ScreenTitle("Repairs")
            }

            if (fullTickets.isEmpty()) {
                Text(
                    "All repairs cleared, good job!",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .semantics { contentDescription = "No repairs found" }
                )
            } else {
                LazyColumn {
                    items(fullTickets) { ticket ->
                        RepairCard(
                            navController = navController,
                            animatedVisibilityScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            db = db,
                            onBackPressed = { /* no-op */ },
                            customerItem = ticket.customer,
                            deviceItem = ticket.deviceWithSpecs.device,
                            repairItem = ticket.repair
                        )
                    }
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
