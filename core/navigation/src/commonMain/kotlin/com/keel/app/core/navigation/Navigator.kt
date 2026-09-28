package com.keel.app.core.navigation

/** Navigation actions available to features. Routes are `@Serializable` types declared in `feature:x:api`. */
interface Navigator {
    fun navigate(route: Any)
    fun back()
}
