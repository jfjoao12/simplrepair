import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.wear.compose.material.ExperimentalWearMaterialApi
import androidx.wear.compose.material.FractionalThreshold
import androidx.wear.compose.material.rememberSwipeableState
import androidx.wear.compose.material.swipeable
import com.example.compose.backgroundDark
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.showRepairID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalWearMaterialApi::class)
@Composable
fun RepairDetailsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    repairItem: Repair,
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

    // Once everything is non-null, render your UI as before:
    val swipeableState = rememberSwipeableState(0)
    val squareSize = 150.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1)
    val offsetDp = with(LocalDensity.current) { swipeableState.offset.value.toDp() }
    val baseHeight = 160.dp

    SharedTransitionLayout {
        with(sharedTransitionScope) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Overlapped header
                Box(Modifier.fillMaxWidth()) {
                    ElevatedCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(20.dp))
                            .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                            .height(baseHeight + offsetDp)
                            .offset { IntOffset(0, swipeableState.offset.value.roundToInt()) },
                        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
                    ) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 72.dp, bottom = 32.dp, start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    "customer",
                                    fontWeight = FontWeight.Thin,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                                Text(
                                    customer!!.customerName,
                                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                                    modifier = Modifier.sharedElement(
                                        sharedTransitionScope.rememberSharedContentState("customerName-${repairItem.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "technician",
                                    fontWeight = FontWeight.Thin,
                                    fontStyle = FontStyle.Italic,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp
                                )
                                Text(
                                    technician!!.name,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.sharedElement(
                                        sharedTransitionScope.rememberSharedContentState("techName-${repairItem.id}"),
                                        animatedVisibilityScope = animatedContentScope
                                    )
                                )
                            }
                        }
                    }

                    ScreenTitle(
                        showRepairID(repairItem.id),
                        Modifier
                            .align(Alignment.TopCenter)
                            .padding(horizontal = 16.dp)
                            .zIndex(1f)
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState("repairId-${repairItem.id}"),
                                animatedVisibilityScope = animatedContentScope
                            )
                    )
                }

                CardSection("Device Details") {
                    Text(
                        "Model: $deviceModel",
                        modifier = Modifier.sharedElement(
                            sharedTransitionScope.rememberSharedContentState("modelName-${repairItem.id}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    )
                    Text("Serial: ${device!!.deviceSerial}")
                }

                CardSection("Repair Info") {
                    Text("Repair Type: ${repairItem.repairType}")
                }

                CardSection("Price") {
                    Text(
                        "$${repairItem.price}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.sharedElement(
                            sharedTransitionScope.rememberSharedContentState("price-${repairItem.id}"),
                            animatedVisibilityScope = animatedContentScope
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun CardSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontWeight = FontWeight.Bold)
            content()
        }
    }
}