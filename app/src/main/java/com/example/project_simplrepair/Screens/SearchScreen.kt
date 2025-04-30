package com.example.project_simplrepair.Screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Layouts.ScreenTitle
import com.example.project_simplrepair.Models.Inventory
import com.example.project_simplrepair.Models.Repair
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * A placeholder screen for search functionality.
 *
 * @param modifier Optional [Modifier] for styling.
 * @param paddingValues Window insets to apply as padding.
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues,
    navController: NavController,
    db: AppDatabase,
) {
    var searchParam by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<Repair>>(emptyList()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        Column {

            Box(
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics { heading() }
            ) {
                ScreenTitle("Search Repair")
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                item {
                    TextField(
                        value = searchParam,
                        onValueChange = { searchParam = it },
                        label = { Text("Search") },
                        singleLine = true,
                        trailingIcon = {
                            IconButton(
                                onClick = {

                                }) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search models"
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                }

            }
        }
    }
}
