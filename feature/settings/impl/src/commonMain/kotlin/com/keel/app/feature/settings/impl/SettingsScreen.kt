package com.keel.app.feature.settings.impl

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keel.app.core.designsystem.component.KeelListItem
import com.keel.app.core.designsystem.component.KeelTopAppBar
import com.keel.app.core.designsystem.theme.KeelTheme
import com.keel.app.core.ui.LoadingContent
import com.keel.app.feature.settings.impl.generated.resources.Res
import com.keel.app.feature.settings.impl.generated.resources.settings_dark_theme
import com.keel.app.feature.settings.impl.generated.resources.settings_dark_theme_description
import com.keel.app.feature.settings.impl.generated.resources.settings_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun SettingsRoute(
    viewModel: SettingsViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SettingsScreen(uiState = uiState, onDarkThemeChange = viewModel::setDarkTheme)
}

@Composable
internal fun SettingsScreen(
    uiState: SettingsUiState,
    onDarkThemeChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { KeelTopAppBar(title = stringResource(Res.string.settings_title)) },
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding)
        when (uiState) {
            SettingsUiState.Loading -> LoadingContent(contentModifier)
            is SettingsUiState.Success -> Column(contentModifier) {
                KeelListItem(
                    headline = stringResource(Res.string.settings_dark_theme),
                    supporting = stringResource(Res.string.settings_dark_theme_description),
                    trailing = { Switch(checked = uiState.isDarkTheme, onCheckedChange = onDarkThemeChange) },
                    onClick = { onDarkThemeChange(!uiState.isDarkTheme) },
                )
            }
        }
    }
}

@Preview
@Composable
private fun SettingsScreenPreview() {
    KeelTheme {
        SettingsScreen(uiState = SettingsUiState.Success(isDarkTheme = false), onDarkThemeChange = {})
    }
}
