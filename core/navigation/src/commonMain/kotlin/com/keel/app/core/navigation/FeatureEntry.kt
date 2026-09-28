package com.keel.app.core.navigation

import androidx.navigation.NavGraphBuilder

/**
 * Entry point of a feature in the navigation graph.
 *
 * Each `feature:x:impl` module binds its implementation in its Koin module; the Shell collects
 * every [FeatureEntry] and registers them in its `NavHost`.
 */
interface FeatureEntry {
    fun register(builder: NavGraphBuilder, navigator: Navigator)
}
