package com.keel.app.core.designsystem.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.core.designsystem.theme.KeelTheme

@Composable
fun KeelNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit,
) {
    NavigationBar(modifier = modifier, content = content)
}

@Composable
fun RowScope.KeelNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: Painter,
    label: String,
    modifier: Modifier = Modifier,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = { Icon(painter = icon, contentDescription = null) },
        label = { Text(label) },
        modifier = modifier,
    )
}

@Preview
@Composable
private fun KeelNavigationBarPreview() {
    KeelTheme {
        KeelNavigationBar {
            KeelNavigationBarItem(selected = true, onClick = {}, icon = KeelIcons.Home, label = "Home")
            KeelNavigationBarItem(selected = false, onClick = {}, icon = KeelIcons.Settings, label = "Settings")
        }
    }
}
