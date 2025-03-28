package com.example.project_simplrepair.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Layouts.GeneralCard
import com.example.project_simplrepair.Models.Repair
import kotlinx.coroutines.launch

@Composable
fun RepairScreen(paddingValues: PaddingValues, navController: NavController, db: AppDatabase) {
    val coroutineScope = rememberCoroutineScope()

    // **State to store repair list**
    var repairList by remember { mutableStateOf(emptyList<Repair>()) }

    // **Fetch data when the screen loads**
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
            Text(
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = "Repairs Screen"
            )

            Spacer(modifier = Modifier.height(16.dp))

            // **Display repairs if available**
            if (repairList.isEmpty()) {
                Text(
                    text = "No repairs found.",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                repairList.forEach { repair ->
                    GeneralCard(navController, repair)
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





