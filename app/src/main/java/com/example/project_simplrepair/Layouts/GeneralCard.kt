package com.example.project_simplrepair.Layouts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Operations.showRepairID
import com.example.project_simplrepair.Operations.taxesCalculation

@Composable
fun GeneralCard(navController: NavHostController, repairItem: Repair) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = {
            navController.navigate("repair_details/${repairItem.id}")
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Name and Repair Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Customer name and repair number
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(repairItem.costumerName, fontWeight = FontWeight.Bold)
                    Text(showRepairID(repairItem.id), fontStyle = FontStyle.Italic)
                }

                // Technician name and repair status
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(repairItem.technicianName)
                    Text("In Repair", fontStyle = FontStyle.Italic)
                }
            }

            // Box with repair type and phone model
            Box(
                modifier = Modifier
                    .padding(top = 20.dp, bottom = 20.dp) // Space from Row
                    .align(Alignment.CenterHorizontally), // Centered horizontally
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(repairItem.repairType.displayName, fontSize = 24.sp)
                    Text(repairItem.model, fontSize = 24.sp)
                }
            }

            // Price
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.End
            ) {
                Text("$${taxesCalculation(repairItem.price)}")
            }
        }
    }
}