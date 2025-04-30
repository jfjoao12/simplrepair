package com.example.project_simplrepair.Screens

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.CustomCardLayout
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.DeviceType
import com.example.project_simplrepair.Operations.showRepairID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Displays detailed information for a specific repair, including:
 * - Customer name
 * - Technician name
 * - Device model and serial
 * - Repair type and price
 *
 * Also supports swipe-down interaction to reveal additional details.
 *
 * @param modifier Optional [Modifier] for screen layout
 * @param paddingValues Insets to apply to the outer layout
 * @param repairItem The selected repair to display
 * @param appDatabase Room database instance
 * @param sharedTransitionScope Scope for shared element transitions
 * @param animatedContentScope Scope for animated content transitions
 * @param onItemClick Callback for handling inner item clicks (currently unused)
 */
@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalWearMaterialApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun RepairDetailsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    repairItem: Repair,
    navController: NavController,
    appDatabase: AppDatabase,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (Int) -> Unit
) {

    // state holders for the related entities
    var customer by remember { mutableStateOf<Customer?>(null) }
    var device by remember { mutableStateOf<Device?>(null) }
    var deviceModel by remember { mutableStateOf<String?>(null) }
    var technician by remember { mutableStateOf<Technician?>(null) }

    // Kick off a one-time load when repairItem.id changes
    LaunchedEffect(repairItem.id) {
        GlobalScope.launch {
            // these DAO calls run off the main thread
            val c = repairItem.id?.let { appDatabase.repairDAO().getCustomerByRepairId(it) }
            val d = repairItem.id?.let { appDatabase.repairDAO().getDeviceByRepairId(it) }
            val t = repairItem.id?.let { appDatabase.repairDAO().getTechByRepairId(it) }
            val dm = appDatabase.deviceDao().getModelNameByDeviceId(d!!.deviceId!!)
            // now post them back to Compose state
            customer = c
            device = d
            technician = t
            deviceModel = dm
        }
    }

    // If any are still loading, show a spinner
    if (customer == null || device == null || technician == null) {
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

    val swipeableState = rememberSwipeableState(0)
    val width = 96.dp
    val squareSize = 150.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to

    // To expand the content down
    var expanded by remember { mutableStateOf(false) }


    with(sharedTransitionScope) {
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
                // Overlapped Group: ScreenTitle and Customer & Technician section
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .swipeable(
                            state = swipeableState,
                            anchors = anchors,
                            thresholds = { _, _ -> FractionalThreshold(0.3f) },
                            orientation = Orientation.Vertical,
                        ),
                ) {
                    // Customer & Technician Card, shifted down a bit so the title overlaps
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(
                                elevation = 4.dp, // Adjust the shadow elevation as needed
                                shape = MaterialTheme.shapes.large, // or choose another shape if you prefer
                                clip = false // set to true if you want to clip the content to the shape
                            )
                            .clip(
                                RoundedCornerShape(
                                    bottomStart = 20.dp,
                                    bottomEnd = 20.dp
                                )
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            )
                            .clickable {expanded = !expanded}
                            .padding(bottom = 10.dp),
                    ) {
                        Column {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        top = 72.dp,
                                        bottom = 6.dp,
                                        start = 20.dp,
                                        end = 20.dp
                                    ),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = customer!!.customerName,
                                    modifier = Modifier
                                        .semantics {
                                            contentDescription =
                                                "Customer ${customer!!.customerName}"
                                        }
                                        .sharedBounds(
                                            sharedTransitionScope.rememberSharedContentState(
                                                "customerName-${repairItem.id}"
                                            ),
                                            animatedVisibilityScope = animatedContentScope
                                        ),
                                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                                )
                                Text(
                                    text = customer!!.customerPhone,
                                    modifier = Modifier
                                        .semantics {
                                            contentDescription =
                                                "Customer ${customer!!.customerPhone}"
                                        },
                                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                                )

                            }

                            AnimatedVisibility(expanded) {
                                Column(
                                    modifier = Modifier
                                        .padding(
                                            start = 20.dp,
                                            end = 20.dp
                                        )
                                ) {
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
                            }
                        }
                    }
                    // ScreenTitle placed at the top and overlapping the card
                    ScreenTitle(
                        showRepairID(repairItem.id),
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp)
                            .zIndex(1f)
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState("repairId-${repairItem.id}"),
                                animatedVisibilityScope = animatedContentScope,
                            )
                    )
                }

                // Device Details Section
                CustomCardLayout("Device Details") {
                    Column {
                        Text(
                            text = "Model: $deviceModel",
                            modifier = Modifier
                                .semantics { contentDescription = "Model $deviceModel" }
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState("modelName-${repairItem.id}"),
                                    animatedVisibilityScope = animatedContentScope
                                )
                        )
                        Text(
                            text = "Serial: ${device!!.deviceSerial}",
                            modifier = Modifier.semantics {
                                contentDescription = "Serial number ${device!!.deviceSerial}"
                            }
                        )
                    }
                }

                CustomCardLayout("Repair Info") {
                    Text(
                        text = "Repair Type: ${repairItem.repairType}",
                        modifier = Modifier.semantics {
                            contentDescription = "Repair type ${repairItem.repairType}"
                        }
                    )
                }

                CustomCardLayout("Price") {
                    Text(
                        text = "$${repairItem.price}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .semantics { contentDescription = "Repair price ${repairItem.price} dollars" }
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState("price-${repairItem.id}"),
                                animatedVisibilityScope = animatedContentScope
                            )
                    )
                }


            }

            Column (
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                SmallFloatingActionButton(
                    onClick = {

                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(bottom = 6.dp)
                        .semantics { contentDescription = "Take Photo of Device" }
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = "Take Photo Icon")
                }

                FloatingActionButton(
                    onClick = {
                        navController.navigate("invoice/${repairItem.id}")

                    },
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .semantics { contentDescription = "Save new repair" }
                ) {
                    Icon(Icons.Filled.AttachMoney, contentDescription = null)
                }
            }
        }
    }
}

/**
 * Card layout with a title and content, used to group sections visually.
 *
 * @param title The title of the card
 * @param content Composable block to display inside the card
 */
@Composable
fun CardSection(title: String, content: @Composable () -> Unit) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(bottom = 8.dp)
                    .semantics { contentDescription = "$title section" }
            )
            content()
        }
    }
}



