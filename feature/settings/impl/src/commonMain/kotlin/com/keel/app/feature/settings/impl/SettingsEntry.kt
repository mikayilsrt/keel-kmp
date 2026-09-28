package com.keel.app.feature.settings.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.core.navigation.Navigator
import com.keel.app.feature.settings.api.SettingsRoute

internal class SettingsEntry : FeatureEntry {
    override fun register(builder: NavGraphBuilder, navigator: Navigator) {
        builder.composable<SettingsRoute> {
            SettingsRoute()
        }
    }
}
