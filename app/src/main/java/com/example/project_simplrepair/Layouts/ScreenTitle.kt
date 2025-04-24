package com.example.project_simplrepair.Layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

/**
 * A stylized title banner for screens.
 *
 * Displays [title] centered inside a rounded-corner container
 * with a secondary background. Automatically marked as a heading
 * for accessibility.
 *
 * @param title The text to show as the screen’s title.
 * @param modifier Optional [Modifier] for further styling or layout tweaks.
 */
@Composable
fun ScreenTitle(
    title: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            // Mark this as a heading so screen readers announce it appropriately
            .semantics { heading() }
            .shadow(
                elevation = 4.dp,
                shape = MaterialTheme.shapes.large,
                clip = false
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .fillMaxWidth()
            .padding(vertical = 16.dp)
            // Let the caller add extra modifiers if desired
            .then(modifier)
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
