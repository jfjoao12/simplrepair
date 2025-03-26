package com.example.project_simplrepair.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.project_simplrepair.DB.AppDatabase

@Composable
fun InsertRepairScreen (
) {
    var name by remember { mutableStateOf("") }
    Box(

    ) {
        OutlinedTextField(
            name,
            onValueChange = { name = it},
            label = { Text("Type of phone") }
        )
        OutlinedTextField(
            name,
            onValueChange = { name = it},
            label = { Text("Type of phone") }
        )
    }
}