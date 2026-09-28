package com.keel.app.shell

import androidx.compose.runtime.Composable

/** Aligns the system bars (status bar icons, iOS interface style) with the in-app theme. */
@Composable
internal expect fun SystemBarAppearance(darkTheme: Boolean)
