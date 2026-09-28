package com.keel.app.shell

import androidx.compose.ui.window.ComposeUIViewController
import platform.UIKit.UIViewController

/** Hosts [App] in UIKit. The SwiftUI side ignores safe areas so that Compose draws edge-to-edge. */
fun MainViewController(): UIViewController = ComposeUIViewController { App() }
