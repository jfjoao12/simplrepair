import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.example.compose.backgroundDark
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Operations.showRepairID

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun RepairDetailsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    repairItem: Repair,
    db: AppDatabase,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    onItemClick: (Int) -> Unit
) {
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
                        .fillMaxWidth()
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
                            .clip (RoundedCornerShape(
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp)
                            )
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                            )

                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                    .padding(top = 72.dp, bottom = 32.dp, start = 20.dp, end = 20.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column{
                                Text(
                                    text = "Customer",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = repairItem.costumerName,
                                    modifier = Modifier.sharedElement(
                                        sharedTransitionScope.rememberSharedContentState(key = "customerName-${repairItem.id}"),
                                        animatedVisibilityScope = animatedContentScope,
                                    )
                                )
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Technician",
                                    fontWeight = FontWeight.Bold
                                )
                                Text(text = repairItem.technicianName)
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
                            text = "Model: ${repairItem.model}",
                            modifier = Modifier
                                .sharedElement(
                                    sharedTransitionScope.rememberSharedContentState("modelName-${repairItem.id}"),
                                    animatedVisibilityScope = animatedContentScope,
                                )
                        )
                        Text(text = "Serial: ${repairItem.serial}")
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