package com.example.project_simplrepair.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Models.Repair

@Composable
fun RepairDetailsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    repairItem: Repair,
    db: AppDatabase
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        LazyColumn (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header
            item {
                Text(
                    text = "Repair Details",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Customer & Technician Details
            item {
                CardSection(title = "Customer & Technician") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "Customer", fontWeight = FontWeight.Bold)
                            Text(text = repairItem.costumerName)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(text = "Technician", fontWeight = FontWeight.Bold)
                            Text(text = repairItem.technicianName)
                        }
                    }
                }
            }

            // Device Details
            item {
                CardSection(title = "Device Details") {
                    Column {
                        Text(text = "Model: ${repairItem.model}")
                        Text(text = "Serial: ${repairItem.serial}")
                    }
                }
            }

            // Repair Information
            item {
                CardSection(title = "Repair Info") {
                    Text(text = "Repair Type: ${repairItem.repairType}")
                }
            }

            // Price Section
            item {
                CardSection(title = "Price") {
                    Text(
                        text = "$${repairItem.price}",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
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
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            content()
        }
    }
}


