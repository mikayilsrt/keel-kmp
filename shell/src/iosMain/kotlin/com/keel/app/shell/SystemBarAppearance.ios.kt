package com.keel.app.shell

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIUserInterfaceStyle

@Composable
internal actual fun SystemBarAppearance(darkTheme: Boolean) {
    val viewController = LocalUIViewController.current
    LaunchedEffect(viewController, darkTheme) {
        // The window is attached after the first frame.
        withFrameNanos { }
        viewController.view.window?.overrideUserInterfaceStyle = if (darkTheme) {
            UIUserInterfaceStyle.UIUserInterfaceStyleDark
        } else {
            UIUserInterfaceStyle.UIUserInterfaceStyleLight
        }
    }
}
