package com.keel.app.shell.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import com.keel.app.core.designsystem.component.KeelNavigationBar
import com.keel.app.core.designsystem.component.KeelNavigationBarItem
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun KeelBottomBar(
    currentDestination: NavDestination?,
    onNavigate: (TopLevelDestination) -> Unit,
    modifier: Modifier = Modifier,
) {
    KeelNavigationBar(modifier = modifier) {
        TopLevelDestination.entries.forEach { destination ->
            KeelNavigationBarItem(
                selected = currentDestination.isOn(destination),
                onClick = { onNavigate(destination) },
                icon = destination.icon,
                label = stringResource(destination.label),
            )
        }
    }
}

internal fun NavDestination?.isOn(destination: TopLevelDestination): Boolean =
    this?.hierarchy?.any { it.hasRoute(destination.routeClass) } == true
