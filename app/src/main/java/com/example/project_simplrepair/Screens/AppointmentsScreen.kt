package com.example.project_simplrepair.Screens

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

/**
 * Composable function to display the Appointments screen.
 *
 * This screen will be shown when users navigate to the Appointments section of the app.
 * It displays a simple message indicating the current screen is for appointments.
 *
 * @param modifier [Modifier] that can be used to modify the layout properties of the composable.
 * @param paddingValues [PaddingValues] that can be used to adjust the padding around the screen content.
 */
@Composable
fun AppointmentsScreen(modifier: Modifier = Modifier, paddingValues: PaddingValues) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            modifier = modifier
                .align(Alignment.Center),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            text = "Appointments Screen",
            color = Color.Black, // Ensures text is visible with proper color contrast
        )
    }
}
