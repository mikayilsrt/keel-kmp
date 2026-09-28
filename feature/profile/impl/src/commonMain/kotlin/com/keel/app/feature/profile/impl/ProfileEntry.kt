package com.keel.app.feature.profile.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.keel.app.core.navigation.FeatureEntry
import com.keel.app.core.navigation.Navigator
import com.keel.app.feature.profile.api.ProfileRoute

internal class ProfileEntry : FeatureEntry {
    override fun register(builder: NavGraphBuilder, navigator: Navigator) {
        builder.composable<ProfileRoute> {
            ProfileRoute(onBackClick = navigator::back)
        }
    }
}
