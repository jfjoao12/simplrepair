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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ScreenTitle(
    title: String,
    modifier: Modifier? = null
){
    Box(
        modifier = Modifier
            .shadow(
                elevation = 4.dp, // Adjust the shadow elevation as needed
                shape = MaterialTheme.shapes.large, // or choose another shape if you prefer
                clip = false // set to true if you want to clip the content to the shape
            )
            .clip(
                RoundedCornerShape(
                    bottomStart = 20.dp,
                    bottomEnd = 20.dp
                )
            )
            .background(MaterialTheme.colorScheme.secondary)
            .fillMaxWidth()
            .padding(
                vertical = 16.dp
            )

    ) {
        Text(
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
            text = title,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}