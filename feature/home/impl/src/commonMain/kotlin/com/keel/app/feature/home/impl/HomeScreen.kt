package com.keel.app.feature.home.impl

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.keel.app.core.designsystem.component.KeelListItem
import com.keel.app.core.designsystem.component.KeelTopAppBar
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.core.designsystem.theme.KeelTheme
import com.keel.app.core.model.User
import com.keel.app.core.ui.EmptyContent
import com.keel.app.core.ui.ErrorContent
import com.keel.app.core.ui.LoadingContent
import com.keel.app.feature.home.impl.generated.resources.Res
import com.keel.app.feature.home.impl.generated.resources.home_empty
import com.keel.app.feature.home.impl.generated.resources.home_error
import com.keel.app.feature.home.impl.generated.resources.home_title
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun HomeRoute(
    onUserClick: (userId: String) -> Unit,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(
        uiState = uiState,
        onUserClick = onUserClick,
        onRetry = viewModel::refresh,
    )
}

@Composable
internal fun HomeScreen(
    uiState: HomeUiState,
    onUserClick: (userId: String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = { KeelTopAppBar(title = stringResource(Res.string.home_title)) },
    ) { padding ->
        val contentModifier = Modifier.fillMaxSize().padding(padding)
        when (uiState) {
            HomeUiState.Loading -> LoadingContent(contentModifier)
            HomeUiState.Error -> ErrorContent(
                message = stringResource(Res.string.home_error),
                onRetry = onRetry,
                modifier = contentModifier,
            )
            is HomeUiState.Success ->
                if (uiState.users.isEmpty()) {
                    EmptyContent(message = stringResource(Res.string.home_empty), modifier = contentModifier)
                } else {
                    UserList(users = uiState.users, onUserClick = onUserClick, modifier = contentModifier)
                }
        }
    }
}

@Composable
private fun UserList(
    users: List<User>,
    onUserClick: (userId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier) {
        items(users, key = User::id) { user ->
            KeelListItem(
                headline = user.name,
                supporting = user.email,
                leadingIcon = KeelIcons.Person,
                trailing = { Icon(painter = KeelIcons.ChevronRight, contentDescription = null) },
                onClick = { onUserClick(user.id) },
            )
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    KeelTheme {
        HomeScreen(
            uiState = HomeUiState.Success(
                listOf(
                    User(id = "1", name = "Ada Lovelace", email = "ada@example.com"),
                    User(id = "2", name = "Alan Turing", email = "alan@example.com"),
                ),
            ),
            onUserClick = {},
            onRetry = {},
        )
    }
}

@Preview
@Composable
private fun HomeScreenErrorPreview() {
    KeelTheme {
        HomeScreen(uiState = HomeUiState.Error, onUserClick = {}, onRetry = {})
    }
}
