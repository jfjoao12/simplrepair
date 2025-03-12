package com.example.project_simplrepair

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.launch

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
                        modifier = Modifier
                            .padding(innerPadding)
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
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scopeMenu = rememberCoroutineScope()
    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                HorizontalDivider()
                TextButton(
                    onClick = {
                        scopeMenu.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                        navController.navigate(Destination.Inventory.route)
                    },
                    content = {
                        Text("Inventory")
                    }

                )
                // Add more items here for navigation if needed
            }
        },
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scopeMenu.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                        }
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = "Navigation Icon"
                            )
                        }
                    },
                    title = { Text("Simpl Repair") },
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
        ) { paddingValues ->
            paddingValues.calculateBottomPadding()
            Spacer(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            )
            NavHost(
                navController = navController as NavHostController,
                startDestination = Destination.Main.route,
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
}