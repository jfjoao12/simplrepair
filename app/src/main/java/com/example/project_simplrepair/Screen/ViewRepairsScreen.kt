package com.example.project_simplrepair.Screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.GeneralCard
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Repair

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
    val coroutineScope = rememberCoroutineScope()


    var repairList by remember { mutableStateOf(emptyList<Repair>()) }

    LaunchedEffect(Unit) {
        repairList = db.repairDAO().getAllRepairs()  // Fetch all repairs from DB
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),

    ) {
        Column(
            modifier =
            Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState()) ,
        ) {
            ScreenTitle("Repairs")


            Spacer(modifier = Modifier.height(16.dp))

            if (repairList.isEmpty()) {
                Text(
                    text = "No repairs found.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                repairList.forEach { repair ->
                        GeneralCard(
                            navController, repair,
                            animatedVisibilityScope = animatedContentScope,
                            sharedTransitionScope = sharedTransitionScope,
                            onBackPressed = {  }
                        )

                }
            }
        }

        FloatingActionButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(20.dp),
            onClick = {
                navController.navigate(Destination.NewRepair.route)
            },
            containerColor = MaterialTheme.colorScheme.primary, // Visible color
            contentColor = MaterialTheme.colorScheme.onPrimary // Icon visible
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add new repair")
        }
    }
}





