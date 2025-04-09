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
import com.example.project_simplrepair.Layouts.fetchCustomerDetails
import com.example.project_simplrepair.Layouts.fetchDeviceDetails
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Device
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Operations.showRepairID
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalWearMaterialApi::class,
    ExperimentalAnimationApi::class
)
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

    val swipeableState = rememberSwipeableState(0)
    val width = 96.dp
    val squareSize = 150.dp
    val sizePx = with(LocalDensity.current) { squareSize.toPx() }
    val anchors = mapOf(0f to 0, sizePx to 1) // Maps anchor points (in px) to

    // To swipe the content down
    val offsetDp = with(LocalDensity.current) { swipeableState.offset.value.toDp() }

    val baseHeight = 160.dp
    val thresholdPx = sizePx / 2

    var showDetails by remember {
        mutableStateOf(false)
    }

    var customer by remember {
        mutableStateOf<Customer?>(null)
    }

    var device by remember {
        mutableStateOf<Device?>(null)
    }

    var technician by remember {
        mutableStateOf<Technician?>(null)
    }

    customer = fetchCustomerDetails(appDatabase, repairItem.id)

    device = fetchDeviceDetails(appDatabase, repairItem.id)


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
                            ),
                            //if (showDetails)
                            //.height(if (showDetails) Modifier.wrapContentHeight() else baseHeight + offsetDp)


                    ) {
//                        if (swipeableState.offset.value > thresholdPx) {
//                            SwipeDownList(
//                                modifier = Modifier
//                                    .sharedElement(
//                                        sharedTransitionScope.rememberSharedContentState("customerName-${repairItem.id}"),
//                                        animatedVisibilityScope = animatedContentScope,
//                                    ),
//                                repairItem = repairItem,
//                                sharedTransitionScope = sharedTransitionScope,
//                                animatedContentScope = animatedContentScope
//                            )
//                        } else



                        LaunchedEffect(swipeableState.offset.value) {
                            showDetails = swipeableState.offset.value > thresholdPx
                        }
                        SharedTransitionLayout{
                            AnimatedContent(
                              targetState = showDetails,

                                label = "pica"
                            ) { targetState ->
                                if (!targetState ){
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                top = 72.dp,
                                                bottom = 32.dp,
                                                start = 20.dp,
                                                end = 20.dp
                                            ),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {

                                            Text(
                                                text = "customer",
                                                fontWeight = FontWeight.Thin,
                                                fontStyle = FontStyle.Italic,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = customer!!.customerName,
                                                modifier = Modifier
                                                    .sharedElement(
                                                        sharedTransitionScope.rememberSharedContentState(
                                                            key = "customerName-${repairItem.id}"
                                                        ),
                                                        animatedVisibilityScope = animatedContentScope,
                                                    ),
                                                fontWeight = MaterialTheme.typography.titleMedium.fontWeight
                                            )

                                        }
                                        Column(
                                            horizontalAlignment = Alignment.End,
                                            modifier = Modifier
                                                .padding(0.dp)
                                        ) {
                                            Text(
                                                modifier = Modifier
                                                    .padding(0.dp),
                                                text = "technician",
                                                fontWeight = FontWeight.Thin,
                                                fontStyle = FontStyle.Italic,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = technician!!.name,
                                                modifier = Modifier
                                                    .padding(0.dp)
                                                    .sharedElement(
                                                        sharedTransitionScope.rememberSharedContentState(
                                                            key = "techName-${repairItem.id}"
                                                        ),
                                                        animatedVisibilityScope = animatedContentScope,
                                                    ),
                                                fontWeight = FontWeight.Bold
                                            )

                                        }
                                    }
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                            .padding(16.dp)
                                            .height(1000.dp)
                                    ) {
                                        Text(
                                            text = customer!!.customerName,
                                            modifier = Modifier
                                                .sharedElement(
                                                    sharedTransitionScope.rememberSharedContentState("customerName-${repairItem.id}"),
                                                    animatedVisibilityScope = animatedContentScope,
                                                )
                                                .align(Alignment.TopCenter)
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
                CardSection(title = "Device Details") {
                    Column {
                        Text(
                            text = "Model: ${device!!.phoneModelId}",
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState("modelName-${repairItem.id}"),
                                    animatedVisibilityScope = animatedContentScope,
                                )
                        )
                        Text(text = "Serial: ${device!!.deviceSerial}")
                    }
                }

                // Repair Information Section
                CardSection(title = "Repair Info") {
                    Text(text = "Repair Type: ${repairItem.repairType}")
                }

                // Price Section
                CardSection(title = "Price") {
                    Text(
                        text = "$${repairItem.price}",
                        fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .sharedElement(
                                sharedTransitionScope.rememberSharedContentState("price-${repairItem.id}"),
                                animatedVisibilityScope = animatedContentScope,
                            )

                    )
                }
            }
        }
    }
}

@Composable
fun CardSection(title: String, content: @Composable () -> Unit) {
    ElevatedCard(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = title,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SwipeDownList(
    modifier: Modifier,
    repairItem: Repair,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
) {
    with(sharedTransitionScope) {
        AnimatedVisibility(
            visible = true, // You can make this conditional as needed
            enter = fadeIn(animationSpec = tween(durationMillis = 300)) +
                    expandVertically(animationSpec = tween(durationMillis = 300)),
            exit = fadeOut(animationSpec = tween(durationMillis = 300)) +
                    shrinkVertically(animationSpec = tween(durationMillis = 300))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Text(
                    text = "FIX!",
                    modifier = Modifier
                        .sharedElement(
                            sharedTransitionScope.rememberSharedContentState("customerName-${repairItem.id}"),
                            animatedVisibilityScope = animatedContentScope,
                        )
                        .align(Alignment.TopCenter)
                )
            }
        }
    }

}