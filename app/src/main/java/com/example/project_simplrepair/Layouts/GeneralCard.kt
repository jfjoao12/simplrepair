package com.example.project_simplrepair.Layouts

import android.icu.text.IDNA.Info
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
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Operations.TaxesCalculation

@Composable
fun GeneralCard(navController: NavController) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        onClick = {navController.navigate(Destination.NewRepair.route)}
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
                    Text("John Doe", fontWeight = FontWeight.Bold)
                    Text("RP00001", fontStyle = FontStyle.Italic)
                }

                // Technician name and repair status
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text("Joao Magalhaes")
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
                    Text("Screen + Battery", fontSize = 24.sp)
                    Text("Galaxy S24 Grey", fontSize = 24.sp)
                }
            }

            // Price
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.End
            ) {
                Text("$${TaxesCalculation(299.99)}")
            }
        }
    }
}