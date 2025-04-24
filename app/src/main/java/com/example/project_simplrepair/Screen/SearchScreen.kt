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
 * A placeholder screen for search functionality.
 *
 * @param modifier Optional [Modifier] for styling.
 * @param paddingValues Window insets to apply as padding.
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            // Announce the screen to accessibility services
            .semantics { contentDescription = "Search Screen" }
    ) {
        Text(
            text = "Search Screen",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .align(Alignment.Center)
                // Mark the title as a heading for screen readers
                .semantics { heading() }
        )
    }
}
