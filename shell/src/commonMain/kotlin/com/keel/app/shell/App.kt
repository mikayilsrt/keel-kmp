package com.keel.app.shell

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.keel.app.core.designsystem.theme.KeelTheme
import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.feature.home.api.HomeRoute
import com.keel.app.shell.navigation.KeelBottomBar
import com.keel.app.shell.navigation.NavControllerNavigator
import com.keel.app.shell.navigation.TopLevelDestination
import com.keel.app.shell.navigation.isOn
import org.koin.compose.getKoin
import org.koin.compose.viewmodel.koinViewModel

/** Root composable shared by Android and iOS. Koin must be started before (see `initKoin`). */
@Composable
fun App(modifier: Modifier = Modifier) {
    val viewModel: AppViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val darkTheme = when (val state = uiState) {
        AppUiState.Loading -> isSystemInDarkTheme()
        is AppUiState.Success -> state.isDarkTheme
    }

    SystemBarAppearance(darkTheme)
    KeelTheme(darkTheme = darkTheme) {
        KeelNavHost(modifier)
    }
}

@Composable
private fun KeelNavHost(modifier: Modifier = Modifier) {
    val koin = getKoin()
    val featureEntries = remember(koin) { koin.getAll<FeatureEntry>() }
    val navController = rememberNavController()
    val navigator = remember(navController) { NavControllerNavigator(navController) }
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination
    val showBottomBar = TopLevelDestination.entries.any { currentDestination.isOn(it) }

    // Edge-to-edge: the Shell consumes no inset itself. Top bars handle the status bar and notch,
    // the bottom bar handles the home indicator / navigation bar, and screens without a bottom bar
    // get the bottom insets through their own Scaffold.
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            if (showBottomBar) {
                KeelBottomBar(
                    currentDestination = currentDestination,
                    onNavigate = navController::navigateToTopLevel,
                )
            }
        },
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = HomeRoute,
            modifier = Modifier.padding(padding).consumeWindowInsets(padding),
        ) {
            featureEntries.forEach { entry -> entry.register(this, navigator) }
        }
    }
}

private fun NavHostController.navigateToTopLevel(destination: TopLevelDestination) {
    navigate(destination.route) {
        popUpTo(graph.findStartDestination().id) { saveState = true }
        launchSingleTop = true
        restoreState = true
    }
}
