package com.example.project_simplrepair.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AppointmentsScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues){
    Box(
        modifier = Modifier
            .fillMaxSize()

    ){
        Text(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier.align(Alignment.Center),
            text="Appointments Screen"
        )
    }
}