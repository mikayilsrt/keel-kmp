package com.keel.app.core.designsystem.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keel.app.core.designsystem.generated.resources.Res
import com.keel.app.core.designsystem.generated.resources.action_back
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.core.designsystem.theme.KeelTheme
import org.jetbrains.compose.resources.stringResource

/** Top app bar. Shows a back arrow when [onBackClick] is provided. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KeelTopAppBar(
    title: String,
    modifier: Modifier = Modifier,
    onBackClick: (() -> Unit)? = null,
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = KeelIcons.ArrowBack,
                        contentDescription = stringResource(Res.string.action_back),
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun KeelTopAppBarPreview() {
    KeelTheme {
        KeelTopAppBar(title = "Title", onBackClick = {})
    }
}
