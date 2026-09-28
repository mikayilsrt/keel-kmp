package com.keel.app.feature.profile.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keel.app.core.designsystem.component.KeelTopAppBar
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.core.designsystem.theme.KeelSpacing
import com.keel.app.core.designsystem.theme.KeelTheme
import com.keel.app.core.model.User
import com.keel.app.core.ui.ErrorContent
import com.keel.app.core.ui.LoadingContent
import com.keel.app.feature.profile.impl.generated.resources.Res
import com.keel.app.feature.profile.impl.generated.resources.profile_not_found
import com.keel.app.feature.profile.impl.generated.resources.profile_title
import com.keel.app.feature.profile.impl.generated.resources.profile_user_id
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ProfileRoute(
    onBackClick: () -> Unit,
    viewModel: ProfileViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ProfileScreen(uiState = uiState, onBackClick = onBackClick)
}

@Composable
internal fun ProfileScreen(
    uiState: ProfileUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            KeelTopAppBar(title = stringResource(Res.string.profile_title), onBackClick = onBackClick)
        },
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding)
        when (uiState) {
            is ProfileUiState.Loading -> LoadingContent(contentModifier)
            is ProfileUiState.NotFound -> ErrorContent(
                message = stringResource(Res.string.profile_not_found),
                modifier = contentModifier,
            )
            is ProfileUiState.Success -> ProfileContent(user = uiState.user, modifier = contentModifier)
        }
    }
}

@Composable
private fun ProfileContent(user: User, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(KeelSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(KeelSpacing.sm),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = KeelIcons.Person,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(96.dp),
        )
        Text(text = user.name, style = MaterialTheme.typography.headlineMedium)
        Text(
            text = user.email,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = stringResource(Res.string.profile_user_id, user.id),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview
@Composable
private fun ProfileScreenPreview() {
    KeelTheme {
        ProfileScreen(
            uiState = ProfileUiState.Success(User(id = "1", name = "Ada Lovelace", email = "ada@example.com")),
            onBackClick = {},
        )
    }
}
