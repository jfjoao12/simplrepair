package com.example.project_simplrepair.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

/**
 * A placeholder screen for showing the inventory.
 *
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param paddingValues Insets to apply for status/navigation bars.
 */
@Composable
fun InventoryScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            // Announce this whole region as the Inventory screen
            .semantics { contentDescription = "Inventory Screen" }
    ) {
        Text(
            text = "Inventory Screen",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .align(Alignment.Center)
                // Mark this text as a heading for screen readers
                .semantics { heading() }
        )
    }
}
