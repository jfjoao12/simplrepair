package com.example.project_simplrepair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Navigation.BottomNav
import com.example.project_simplrepair.Screen.AppointmentsScreen
import com.example.project_simplrepair.Screen.InventoryScreen
import com.example.project_simplrepair.Screen.RepairScreen
import com.example.project_simplrepair.Screen.SettingsScreen
import com.example.project_simplrepair.ui.theme.ProjectSimplRepairTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectSimplRepairTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize()
                ) { innerPadding ->
                    val navController = rememberNavController()

                    App(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App (navController: NavController, modifier: Modifier) {
//    var option by remember {
//        mutableStateOf()
//    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Simpl Repair")},
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate(Destination.Settings.route)
                        }
                    ) {
                        Icon(
                            Icons.Rounded.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNav(navController = navController) }
    ){ paddingValues ->
        paddingValues.calculateBottomPadding()
        Spacer(modifier = Modifier.padding(paddingValues))
        NavHost(
            navController= navController as NavHostController,
            startDestination = Destination.Main.route
        ) {
            composable(Destination.Main.route) {
                RepairScreen(paddingValues)
            }
            composable(Destination.Settings.route) {
                SettingsScreen(modifier, paddingValues)
            }
            composable(Destination.Inventory.route) {
                InventoryScreen(modifier, paddingValues)
            }
            composable(Destination.Appointments.route) {
                AppointmentsScreen(modifier, paddingValues)
            }
        }

    }
}