package com.keel.app.shell

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.keel.app.core.designsystem.generated.resources.Res
import com.keel.app.core.designsystem.generated.resources.app_name
import com.keel.app.core.designsystem.theme.KeelTheme
import org.jetbrains.compose.resources.stringResource

@Composable
@Preview
fun App() {
    KeelTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Box(contentAlignment = Alignment.Center) {
                Text(stringResource(Res.string.app_name))
            }
        }
    }
}
