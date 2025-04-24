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
 * A placeholder screen for app settings.
 *
 * @param modifier Optional [Modifier] for styling.
 * @param paddingValues Window insets to apply as padding.
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            // Provide an accessible label for the entire screen
            .semantics { contentDescription = "Settings Screen" }
    ) {
        Text(
            text = "Settings Screen",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .align(Alignment.Center)
                // Mark this text as a heading for assistive technologies
                .semantics { heading() }
        )
    }
}
