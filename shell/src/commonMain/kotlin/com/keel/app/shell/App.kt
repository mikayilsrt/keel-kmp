package com.keel.app.shell

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.keel.app.core.designsystem.theme.KeelTheme
import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.feature.home.api.HomeRoute
import com.keel.app.shell.navigation.NavControllerNavigator
import org.koin.compose.getKoin

/** Root composable shared by Android and iOS. Koin must be started before (see `initKoin`). */
@Composable
fun App(modifier: Modifier = Modifier) {
    val koin = getKoin()
    val featureEntries = remember(koin) { koin.getAll<FeatureEntry>() }
    val navController = rememberNavController()
    val navigator = remember(navController) { NavControllerNavigator(navController) }

    KeelTheme {
        // Edge-to-edge: the Shell consumes no inset itself; top bars and bottom bar handle
        // the status bar, notch, home indicator and navigation bar insets.
        Scaffold(
            modifier = modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0),
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
}
