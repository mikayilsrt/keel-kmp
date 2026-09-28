package com.keel.app.feature.home.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.core.navigation.Navigator
import com.keel.app.feature.home.api.HomeRoute
import com.keel.app.feature.profile.api.ProfileRoute

internal class HomeEntry : FeatureEntry {
    override fun register(builder: NavGraphBuilder, navigator: Navigator) {
        builder.composable<HomeRoute> {
            HomeRoute(onUserClick = { userId -> navigator.navigate(ProfileRoute(userId)) })
        }
    }
}
