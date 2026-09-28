package com.keel.app.shell.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.feature.home.api.HomeRoute
import com.keel.app.feature.settings.api.SettingsRoute
import com.keel.app.shell.generated.resources.Res
import com.keel.app.shell.generated.resources.destination_home
import com.keel.app.shell.generated.resources.destination_settings
import org.jetbrains.compose.resources.StringResource
import kotlin.reflect.KClass

/** Destinations shown in the bottom bar. */
internal enum class TopLevelDestination(
    val route: Any,
    val routeClass: KClass<*>,
    val label: StringResource,
) {
    Home(HomeRoute, HomeRoute::class, Res.string.destination_home),
    Settings(SettingsRoute, SettingsRoute::class, Res.string.destination_settings),
    ;

    val icon: Painter
        @Composable get() = when (this) {
            Home -> KeelIcons.Home
            Settings -> KeelIcons.Settings
        }
}
