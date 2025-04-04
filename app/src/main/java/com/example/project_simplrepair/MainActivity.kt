package com.example.project_simplrepair

import RepairDetailsScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.compose.ProjectSimplRepairTheme
import com.example.project_simplrepair.API.PhonesApiManager
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Models.PhoneBrands
import com.example.project_simplrepair.Models.PhoneSpecs
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Navigation.BottomNav
import com.example.project_simplrepair.Screen.AppointmentsScreen
import com.example.project_simplrepair.Screen.InsertCustomerScreen
import com.example.project_simplrepair.Screen.InsertRepairScreen
import com.example.project_simplrepair.Screen.InventoryScreen

import com.example.project_simplrepair.Screen.RepairScreen
import com.example.project_simplrepair.Screen.SearchScreen
import com.example.project_simplrepair.Screen.SettingsScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectSimplRepairTheme {
                Scaffold (
                    modifier = Modifier
                        .fillMaxSize(),

                ) { innerPadding ->
                    val navController = rememberNavController()
                    val db = AppDatabase.getInstance(applicationContext)
                    val allPhoneBrands = PhonesApiManager(db)

                    allPhoneBrands.getPhoneSpecs("Apple", "iPhone 13 Pro Max", db)

                    App(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding),
                        db = db
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
    ExperimentalSharedTransitionApi::class
)


@Composable
fun App (navController: NavController, modifier: Modifier, db: AppDatabase) {
    var repair by remember {
        mutableStateOf<Repair?>(null)
    }
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

                TextButton(
                    onClick = {
                        scopeMenu.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                        navController.navigate(Destination.NewCustomer.route)
                    },
                    content = {
                        Text("New Customer")
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
                    title = {
                        Text(
                            text = "Simpl Repair",
                            style = MaterialTheme.typography.titleLarge,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Bold
                        ) },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary, // Your custom background color here
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        scrolledContainerColor = MaterialTheme.colorScheme.onPrimary


                    ),
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
                    },
                    modifier = Modifier
                        .shadow(
                            elevation = 4.dp, // Adjust the shadow elevation as needed
                            shape = MaterialTheme.shapes.medium, // or choose another shape if you prefer
                            clip = false // set to true if you want to clip the content to the shape
                        )
                        .background(MaterialTheme.colorScheme.onPrimary)

                )

            },
            bottomBar = { BottomNav(navController = navController) }

        ) {
            paddingValues ->
            paddingValues.calculateBottomPadding()

            Spacer(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxWidth()
            )
            SharedTransitionLayout {
                NavHost(
                    navController = navController as NavHostController,
                    startDestination = Destination.Main.route,
                ) {
                    composable(Destination.Main.route) {
                        RepairScreen(
                            paddingValues = paddingValues,
                            navController = navController,
                            db = db,
                            sharedTransitionScope = this@SharedTransitionLayout,
                            animatedContentScope = this,
                            onItemClick = { repairId -> navController.navigate("repair_details/$repairId") }
                        )
                    }
                    composable(Destination.Settings.route) {
                        SettingsScreen(modifier, paddingValues)
                    }
                    composable(Destination.Inventory.route) {
                        InventoryScreen(modifier, paddingValues)
                    }
                    composable(Destination.RepairDetails.route) { navBackStackEntry ->
                        var repairId: String? = navBackStackEntry.arguments?.getString("repair_id")
                        GlobalScope.launch {
                            if (repairId != null) {
                                repair = db.repairDAO().getRepairById(repairId.toInt())
                            }
                        }
                        repair?.let {
                            RepairDetailsScreen(
                                modifier = Modifier.padding(paddingValues),
                                repairItem = it,
                                db = db,
                                paddingValues = paddingValues,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                onItemClick = {
                                }
                            )
                        }
                    }
                    composable(Destination.Appointments.route) {
                        AppointmentsScreen(modifier, paddingValues)
                    }
                    composable(Destination.Search.route) {
                        SearchScreen(modifier, paddingValues)
                    }
                    composable(Destination.NewRepair.route) {
                        InsertRepairScreen(
                            paddingValues,
                            AppDatabase.getInstance(context = LocalContext.current),
                            navController
                        )
                    }
                    composable(Destination.NewCustomer.route) {
                        InsertCustomerScreen(
                            paddingValues,
                            AppDatabase.getInstance(context = LocalContext.current),
                            navController
                        )
                    }
                }
            }

        }
    }
}