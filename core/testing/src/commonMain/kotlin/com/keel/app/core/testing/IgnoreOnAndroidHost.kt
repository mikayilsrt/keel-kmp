package com.keel.app.core.testing

/**
 * Skips a common test on the Android host (JVM), where `android.os.Bundle` is only a stub.
 * Needed for code relying on `SavedStateHandle.toRoute()`; the test still runs on iOS.
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
expect annotation class IgnoreOnAndroidHost()
