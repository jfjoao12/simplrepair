package com.example.project_simplrepair.Screens.Repair

import android.graphics.BitmapFactory.decodeFile
import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Layouts.CustomCardLayout
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.DevicePhoto
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.RepairStatus
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.showRepairID
import com.example.project_simplrepair.hilt.TicketViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

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
    repairId: Int,
    navController: NavController,
    appDatabase: AppDatabase,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    ticketVm: TicketViewModel = hiltViewModel(),
    onItemClick: (Int) -> Unit
) {

    LaunchedEffect (repairId) {
        ticketVm.loadFullTicket(repairId)
    }
    // obtain ViewModel instance
    val ticket by ticketVm.fullTicket.collectAsState()


    // state holders for the related entities
    var technician by remember { mutableStateOf<Technician?>(null) }
    var devicePhotos = remember { mutableStateListOf<String?>() }
    var boxExpanded by remember { mutableStateOf(false) }
    var boxHeight by remember { mutableIntStateOf(0) }
    var editCard by remember { mutableStateOf(false) }



    // Kick off a one-time load when ticket!!.repair.id changes
//    LaunchedEffect(ticket!!.repair.id) {
//        GlobalScope.launch {
//            // these DAO calls run off the main thread
//            val c = ticket!!.repair.id?.let { appDatabase.customerDao().getCustomerByRepairId(it) }
//            val d = ticket!!.repair.id?.let { appDatabase.deviceDao().getDeviceByRepairId(it) }
//            val t = ticket!!.repair.id?.let { appDatabase.repairDAO().getTechByRepairId(it) }
//            val dm = appDatabase.deviceDao().getModelNameByDeviceId(d!!.deviceId!!)
//            val fp = appDatabase.devicePhotoDao().getPhotoPathsForRepair(ticket!!.repair.id)
//            // now post them back to Compose state
//            customer = c
//            device = d
//            technician = t
//            deviceModel = dm
//            fp.forEach() { path ->
//                devicePhotos += path
//            }
//        }
//    }

    // If any are still loading, show a spinner
    if (ticket == null) {
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


    SharedTransitionLayout (
        modifier = Modifier
            .fillMaxSize()
    ){
        Box(
            modifier = Modifier
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
                                    text = ticket!!.customer.customerName,
                                    modifier = Modifier
                                        .semantics {
                                            contentDescription =
                                                "Customer ${ticket!!.customer.customerName}"
                                        }
                                        .sharedBounds(
                                            sharedTransitionScope.rememberSharedContentState(
                                                "customerName-${ticket!!.repair.id}"
                                            ),
                                            animatedVisibilityScope = animatedContentScope
                                        ),
                                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                                )
                                Text(
                                    text = ticket!!.customer.customerPhone,
                                    modifier = Modifier
                                        .semantics {
                                            contentDescription =
                                                "Customer ${ticket!!.customer.customerPhone}"
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
                                        ticket!!.customer.customerEmail?.let { email ->
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
                                            text = ticket!!.customer.customerAddress.toString(),
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
                                            text = "${ticket!!.customer.customerCity}, ${ticket!!.customer.customerProv} ${ticket!!.customer.customerPostalCode}",
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }
                    }
                    // ScreenTitle placed at the top and overlapping the card
                    ScreenTitle(
                        showRepairID(repairId),
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp)
                            .zIndex(1f)
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState("repairId-${repairId}"),
                                animatedVisibilityScope = animatedContentScope,
                            )
                    )
                }

                    AnimatedVisibility(
                        visible = !editCard,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                    ) {
                    Column(
                        modifier = Modifier
                            .sharedBounds(
                                sharedContentState = rememberSharedContentState(key = "RepairDetails-bounds"),
                                // Using the scope provided by AnimatedVisibility
                                animatedVisibilityScope = this,
                                clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp))
                            )
                    ) {
                        ExpandablePhotoSection(
                            title = "Gallery",
                            devicePhotos = ticket!!.photos,
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "repairInfo-bounds"),
                                    // Using the scope provided by AnimatedVisibility
                                    animatedVisibilityScope = this@AnimatedVisibility
                                ),
                        )

                        CustomCardLayout(
                            title = "Repair Info",
                            editable = true,
                            onEditClick = {
                                editCard = true
                            },
                            modifier = Modifier
                                .sharedBounds(
                                    sharedContentState = rememberSharedContentState(key = "repairInfo-bounds"),
                                    // Using the scope provided by AnimatedVisibility
                                    animatedVisibilityScope = this@AnimatedVisibility,
                                )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Status:  ",
                                    modifier = Modifier
                                        .semantics { contentDescription = "Repair Status" }
                                )
                                Text(
                                    text = ticket!!.repair.repairStatus.displayName,
                                    modifier = Modifier.semantics {
                                        contentDescription = "Repair Status"
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Repair Type: ",
                                    modifier = Modifier.semantics {
                                        contentDescription = "Repair type ${ticket!!.repair.repairType}"
                                    }
                                )

                                Text(
                                    text = ticket!!.repair.repairType.displayName,
                                    modifier = Modifier.semantics {
                                        contentDescription = "Repair type ${ticket!!.repair.repairType}"
                                    }
                                )

                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Quote: ",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .semantics { contentDescription = "Repair price quotation" }
                                )

                                Text(
                                    text = ticket!!.repair.price.toString(),
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier
                                        .semantics { contentDescription = "Repair price value" }
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .padding(top = 16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(bottom = 8.dp),
                                    text = "Notes",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .padding(16.dp)
                                ) {
                                    Text(
                                        text = ticket!!.repair.notes,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }
                        }
                        CustomCardLayout("Device Details") {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Model: ",
                                    modifier = Modifier
                                        .semantics { contentDescription = "Model ${ticket!!.deviceWithSpecs.specs.name}" }
                                        .sharedElement(
                                            sharedTransitionScope.rememberSharedContentState("modelName-pica"),
                                            animatedVisibilityScope = animatedContentScope
                                        )
                                )
                                Text(
                                    text = "placeholder",
                                    modifier = Modifier.semantics {
                                        contentDescription = "Device Model"
                                    }
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Serial: ",
                                    modifier = Modifier
                                        .semantics { contentDescription = "Model " }
                                )
                                Text(
                                    text = ticket!!.deviceWithSpecs.device.deviceSerial,
                                    modifier = Modifier.semantics {
                                        contentDescription = "Device Model"
                                    }
                                )
                            }
                        }
                    }
                }

                    AnimatedVisibility(
                        visible = editCard,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut() + scaleOut(),
                    ){
                        CustomCardLayout(
                            title = "Edit Device Details",
                            editable = true,
                            onEditClick = {editCard = false},
                        ) {

                        }

                    }
            }

            // If repair is completed, it won't show the FABs
            if (ticket!!.repair.repairStatus != RepairStatus.COMPLETED) {
                val rotation by animateFloatAsState(
                    targetValue   = if (editCard) 360f else 0f,
                    animationSpec = tween(durationMillis = 300)
                )
                Column (
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(20.dp),

                ) {
                        AnimatedVisibility(
                            visible = !editCard,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut(),
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    navController.navigate("invoice/${ticket!!.repair.id}")

                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "repairInfo-FAB"),
                                        animatedVisibilityScope = this,
                                        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp))
                                    )
                                    .semantics { contentDescription = "Save new repair" }

                            ) {
                                Icon(
                                    Icons.Filled.AttachMoney,
                                    contentDescription = null,
                                    modifier = Modifier

                                        .graphicsLayer { rotationZ = rotation } // ← apply your spin

                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = editCard,
                            enter = fadeIn() + scaleIn(),
                            exit = fadeOut() + scaleOut(),
                        ) {
                            FloatingActionButton(
                                onClick = {
                                    editCard = false
                                },
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier
                                    .sharedBounds(
                                        sharedContentState = rememberSharedContentState(key = "repairInfo-FAB"),
                                        animatedVisibilityScope = this,
                                        clipInOverlayDuringTransition = OverlayClip(RoundedCornerShape(16.dp))
                                    )
                                    .semantics { contentDescription = "Save Changes" }

                            ) {
                                Icon(
                                    Icons.Filled.Check,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .graphicsLayer { rotationZ = rotation } // ← apply your spin
                                )
                            }
                        }
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ExpandablePhotoSection(
    title: String,
    devicePhotos: List<DevicePhoto>,
    collapsedThumbSize: Dp = 128.dp,
    expandedImageSize: Dp = 256.dp,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    CustomCardLayout (
        "Device Photos"
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header row: title + expand/collapse icon
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium
                        .copy(fontWeight = FontWeight.Bold)
                )
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = Icons.Rounded.ExpandMore,
                        contentDescription = if (expanded) "Collapse photos" else "Expand photos",
                        modifier = Modifier.rotate(if (expanded) 180f else 0f)
                    )
                }
            }

            // Animated switch between collapsed thumbnails and expanded images
            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = shrinkVertically() + fadeOut()
            ) {
                // Expanded: one big column of larger images
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    devicePhotos.forEach { path ->
                        val bmp = remember(path) { decodeFile(path.filePath) }
                        if (bmp != null) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "",
                                modifier = Modifier
                                    .rotate(90f)
                                    .size(expandedImageSize)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = !expanded,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                // Collapsed: horizontal row of thumbnails
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(devicePhotos) { path ->
                        val bmp = remember(path) { decodeFile(path.filePath) }
                        if (bmp != null) {
                            Image(
                                bitmap = bmp.asImageBitmap(),
                                contentDescription = "Device photo thumbnail",
                                modifier = Modifier
                                    .rotate(90f)
                                    .size(collapsedThumbSize)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                            )
                        }
                    }
                }
            }
        }
    }
}




