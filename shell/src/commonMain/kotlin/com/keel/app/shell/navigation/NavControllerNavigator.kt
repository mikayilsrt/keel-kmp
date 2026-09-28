package com.keel.app.shell.navigation

import androidx.navigation.NavHostController
import com.keel.app.core.navigation.Navigator

internal class NavControllerNavigator(
    private val navController: NavHostController,
) : Navigator {
    override fun navigate(route: Any) {
        navController.navigate(route)
    }

    override fun back() {
        navController.popBackStack()
    }
}
