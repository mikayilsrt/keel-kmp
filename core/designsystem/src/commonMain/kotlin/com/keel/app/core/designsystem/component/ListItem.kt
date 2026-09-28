package com.keel.app.core.designsystem.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.tooling.preview.Preview
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.core.designsystem.theme.KeelTheme

@Composable
fun KeelListItem(
    headline: String,
    modifier: Modifier = Modifier,
    supporting: String? = null,
    leadingIcon: Painter? = null,
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null,
) {
    ListItem(
        headlineContent = { Text(headline) },
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        supportingContent = supporting?.let { { Text(it) } },
        leadingContent = leadingIcon?.let { { Icon(painter = it, contentDescription = null) } },
        trailingContent = trailing,
    )
}

@Preview
@Composable
private fun KeelListItemPreview() {
    KeelTheme {
        KeelListItem(
            headline = "Headline",
            supporting = "Supporting text",
            leadingIcon = KeelIcons.Person,
            onClick = {},
        )
    }
}
