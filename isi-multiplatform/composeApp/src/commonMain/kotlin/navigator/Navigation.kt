package navigator

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import moe.tlaster.precompose.navigation.NavHost
import moe.tlaster.precompose.navigation.Navigator
import moe.tlaster.precompose.viewmodel.viewModel
import presentation.IsiViewModel
import presentation.LocalIsiViewModel
import ui.screens.*
import utils.IntentSpeechToText

sealed class Screen(val route: String, val icon: ImageVector, val label: String) {
    /** Bottom Bar access **/
    object Home : Screen("/home", Icons.Default.Home, "Home")
    object Explore : Screen("/explore", Icons.Default.Explore, "Explore")
    object Isi : Screen("/isi", Icons.AutoMirrored.Filled.Chat, "ISI")
    object Settings : Screen("/settings", Icons.Default.Settings, "Settings")

    /** END Bottom Bar access **/

    object CarCoords : Screen("/car-coords", Icons.Default.DirectionsCar, "Car coords")
    object VoiceCommands : Screen("/voice-command", Icons.Default.Settings, "ISI")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Navigation(navigator: Navigator, intentSpeechToText: IntentSpeechToText? = null) {
    val viewModel = viewModel(modelClass = IsiViewModel::class) {
        IsiViewModel()
    }

    val canGoBack by navigator.canGoBack.collectAsState(initial = false)

    val initialRoute: String =
        if (intentSpeechToText != null && intentSpeechToText.equals(IntentSpeechToText.ACTION)) Screen.VoiceCommands.route else Screen.Home.route


    var currentRoute by remember { mutableStateOf<String>("") }

    LaunchedEffect(Unit) {
        navigator.currentEntry.collect { entry ->
            entry?.let {
                currentRoute = it.route.route
            }
        }
    }

    Scaffold(
        topBar = {
            TopNavigationBar(canGoBack, navigator)
        },
        bottomBar = {
            BottomNavigationBar(navigator, currentRoute)
        },
    ) { paddingValues ->
        CompositionLocalProvider(LocalIsiViewModel provides viewModel) {
            NavHost(
                navigator = navigator,
                initialRoute = initialRoute,
                modifier = Modifier.padding(paddingValues)
            ) {
                scene(route = Screen.Home.route) {
                    HomeScreen(goTo = navigator::navigate)
                }
                scene(route = Screen.Explore.route) {
                    ExploreScreen(goTo = navigator::navigate)
                }
                scene(route = Screen.Settings.route) {
                    SettingsScreen()
                }
                scene(route = Screen.VoiceCommands.route) {
                    VoiceCommandScreen(goTo = navigator::navigate)
                }
                scene(route = Screen.CarCoords.route) {
                    CarCoordsScreen()
                }
                scene(route = Screen.Isi.route) {
                    IsiChatScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopNavigationBar(canGoBack: Boolean, navigator: Navigator) {
    TopAppBar(
        title = {},
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = { navigator.goBack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun BottomNavigationBar(navigator: Navigator, currentRoute: String) {
    if (currentRoute == Screen.Isi.route) {
        return
    }
    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        listOf(Screen.Home, Screen.Explore, Screen.Isi, Screen.Settings).forEach { screen ->
            BottomNavigationItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = { navigator.navigate(screen.route) },
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}