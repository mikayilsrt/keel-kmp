package com.keel.app.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.keel.app.core.designsystem.component.KeelButton
import com.keel.app.core.designsystem.generated.resources.Res
import com.keel.app.core.designsystem.generated.resources.action_retry
import com.keel.app.core.designsystem.icon.KeelIcons
import com.keel.app.core.designsystem.theme.KeelSpacing
import com.keel.app.core.designsystem.theme.KeelTheme
import org.jetbrains.compose.resources.stringResource

/** Full-size error state with an optional retry action. */
@Composable
fun ErrorContent(
    message: String,
    modifier: Modifier = Modifier,
    onRetry: (() -> Unit)? = null,
) {
    Column(
        modifier = modifier.fillMaxSize().padding(KeelSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(KeelSpacing.md, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = KeelIcons.Error,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error,
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
        )
        if (onRetry != null) {
            KeelButton(text = stringResource(Res.string.action_retry), onClick = onRetry)
        }
    }
}

@Preview
@Composable
private fun ErrorContentPreview() {
    KeelTheme {
        ErrorContent(message = "Something went wrong", onRetry = {})
    }
}
