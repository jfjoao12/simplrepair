package com.example.project_simplrepair

import com.example.project_simplrepair.Screens.Repair.RepairDetailsScreen
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material3.Icon
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
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.compose.ProjectSimplRepairTheme
import com.example.project_simplrepair.API.PhonesApiManager
import com.example.project_simplrepair.ViewModels.PhotoViewModel
import com.example.project_simplrepair.DB.AppDatabase
import com.example.project_simplrepair.Destination.Destination
import com.example.project_simplrepair.Models.Customer
import com.example.project_simplrepair.Models.Repair
import com.example.project_simplrepair.Models.Technician
import com.example.project_simplrepair.Navigation.BottomNav
import com.example.project_simplrepair.Screens.AppointmentsScreen
import com.example.project_simplrepair.Screens.Repair.CameraScreen
import com.example.project_simplrepair.Screens.InsertCustomerScreen
import com.example.project_simplrepair.Screens.Repair.InsertRepairScreen
import com.example.project_simplrepair.Screens.Inventory.InventoryScreen
import com.example.project_simplrepair.Screens.Inventory.NewInventoryItemScreen
import com.example.project_simplrepair.Screens.InvoiceScreen

import com.example.project_simplrepair.Screens.Repair.RepairScreen
import com.example.project_simplrepair.Screens.SearchScreen
import com.example.project_simplrepair.Screens.SettingsScreen
import com.example.project_simplrepair.ViewModels.InsertRepairViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
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

//                    allPhoneBrands.getPhoneSpecs(brandId = 66, database = db)
//                    val cameraPermissionState: PermissionState =
//                        rememberPermissionState(android.Manifest.permission.CAMERA)
//                    allPhoneBrands.getPhoneSpecs("Apple", "iPhone 13 Pro Max", db)
//
                    GlobalScope.launch {
                        db.technicianDao().insert(
                            Technician(
                                id = null,
                                name = "Joao Magalhaes"
                            )
                        )
                        db.customerDao().insert(
                            Customer(
                                customerId = null,
                                customerName = "James Bond",
                                customerEmail = "jbond007@rrc.ca",
                                customerCity = "Winnipeg",
                                customerCountry = "Canada",
                                customerPostalCode = "R0H0H0",
                                customerProv = "MB",
                                customerAddress = "123 That Street",
                                customerAddressTwo = "Unit 007",
                                customerPhone = "2047000007",
                                customerPhoneTwo = ""
                            )
                        )
                    }




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
@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class, DelicateCoroutinesApi::class,
    ExperimentalSharedTransitionApi::class)
@Composable
fun App (navController: NavController, modifier: Modifier, db: AppDatabase) {
    var repair by remember {
        mutableStateOf<Repair?>(null)
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scopeMenu = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBars = currentRoute != "Camera"


    var showCamera by remember { mutableStateOf(false) }
    //var photoPaths by remember { mutableStateOf<List<String>>(emptyList()) }
    val newPhotoPaths = remember { mutableStateListOf<String>() }
    val photoVm: PhotoViewModel = viewModel()


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "App Logo",
                        modifier = Modifier.size(220.dp)
                    )
                }

                HorizontalDivider()

                //––– Inventory Link –––

                //––– New Customer Link –––
                NavigationDrawerItem(
                    label = { Text("New Customer") },
                    selected = false,
                    onClick = {
                        scopeMenu.launch {
                            drawerState.apply {
                                close()
                            }
                        }
                        navController.navigate(Destination.NewCustomer.route)
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = "New Customer"
                        )
                    },
                    modifier = Modifier
                        .padding(NavigationDrawerItemDefaults.ItemPadding)
                        .align(Alignment.CenterHorizontally)
                )
            }
        },
    ) {
        Scaffold(
            topBar = {
                if (showBars) {
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
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            scrolledContainerColor = MaterialTheme.colorScheme.onPrimaryContainer
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
                }

            },
            bottomBar = {
                if (showBars) {
                    BottomNav(navController = navController)
                }
            }

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

                    composable(Destination.Settings.route) {
                        SettingsScreen(modifier, paddingValues)
                    }
                    composable(Destination.Inventory.route) {
                        InventoryScreen(modifier, paddingValues, navController, db)
                    }
                    composable(Destination.Invoice.route){ navBackStackEntry ->
                        var repairId: String? = navBackStackEntry.arguments?.getString("repair_id")
                        GlobalScope.launch {
                            if(repairId != null){
                                repair = db.repairDAO().getRepairById(repairId.toInt())
                            }
                        }
                        repair?.let{
                            InvoiceScreen(modifier, it, paddingValues, navController, db)

                        }
                    }

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
                                paddingValues = paddingValues,
                                navController = navController,
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedContentScope = this,
                                appDatabase = db,
                                onItemClick = {
                                },
                            )
                        }
                    }
                    composable(Destination.Appointments.route) {
                        AppointmentsScreen(modifier, paddingValues)
                    }
                    composable(Destination.Search.route) {
                        SearchScreen(
                            modifier,
                            paddingValues,
                            navController,
                            db,
                        )
                    }
                    composable(Destination.EditRepair.route){ navBackStackEntry ->
                        var repairId: String? = navBackStackEntry.arguments?.getString("repair_id")
                        GlobalScope.launch {
                            if(repairId != null){
                                repair = db.repairDAO().getRepairById(repairId.toInt())
                            }
                        }
                        repair?.let{
                            val photoVm: PhotoViewModel = navBackStackEntry.sharedViewModel(navController)
                            val insertVm: InsertRepairViewModel =
                                viewModel(navBackStackEntry)
                            InsertRepairScreen(
                                paddingValues = paddingValues,
                                appDatabase = AppDatabase.getInstance(LocalContext.current),
                                navController = navController,
                                photoPaths = photoVm.photoPaths,
                                insertVm = insertVm,
                                repairItem = repair!!
                            )
                        }
                    }
                    // Handle navigation between these 2 screens to persist data
                    navigation(
                        startDestination = Destination.NewRepair.route,
                        route = "insertFlow"
                    ) {

                        composable(Destination.NewRepair.route) { backStackEntry  ->
                            // 1) get the *same* navGraph entry for the VM:
                            val photoVm: PhotoViewModel = backStackEntry.sharedViewModel(navController)
                            val insertVm: InsertRepairViewModel =
                                viewModel(backStackEntry)

                            InsertRepairScreen(
                                paddingValues = paddingValues,
                                appDatabase = AppDatabase.getInstance(LocalContext.current),
                                navController = navController,
                                photoPaths = photoVm.photoPaths,
                                insertVm = insertVm,
                            )
                        }

                        composable(Destination.Camera.route) { backStackEntry ->
                            // again scope to the graph, not this screen
                            val photoVm: PhotoViewModel = backStackEntry.sharedViewModel(navController)

                            CameraScreen(
                                onPhotoTaken = { path ->
                                    photoVm.add(path)
                                },
                                onCancel = {
                                    navController.popBackStack()
                                },
                                db = db
                            )
                        }
                        composable(Destination.NewCustomer.route) {
                            InsertCustomerScreen(
                                paddingValues,
                                AppDatabase.getInstance(context = LocalContext.current),
                                navController
                            )
                        }

                        composable(Destination.NewInventoryItem.route) {
                            NewInventoryItemScreen(
                                paddingValues,
                                navController,
                                AppDatabase.getInstance(context = LocalContext.current)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
inline fun <reified T: ViewModel> NavBackStackEntry.sharedViewModel(
    navController: NavHostController,
): T {
    val navGraphRoute = destination.parent?.route ?: return viewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return viewModel(parentEntry)
}