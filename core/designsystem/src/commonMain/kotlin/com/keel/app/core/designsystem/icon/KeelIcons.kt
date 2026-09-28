package com.keel.app.core.designsystem.icon

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import com.keel.app.core.designsystem.generated.resources.Res
import com.keel.app.core.designsystem.generated.resources.ic_arrow_back
import com.keel.app.core.designsystem.generated.resources.ic_chevron_right
import com.keel.app.core.designsystem.generated.resources.ic_error
import com.keel.app.core.designsystem.generated.resources.ic_home
import com.keel.app.core.designsystem.generated.resources.ic_person
import com.keel.app.core.designsystem.generated.resources.ic_settings
import org.jetbrains.compose.resources.painterResource

/** Icons of the design system, backed by vector drawables in Compose Resources. */
object KeelIcons {
    val ArrowBack: Painter @Composable get() = painterResource(Res.drawable.ic_arrow_back)
    val ChevronRight: Painter @Composable get() = painterResource(Res.drawable.ic_chevron_right)
    val Error: Painter @Composable get() = painterResource(Res.drawable.ic_error)
    val Home: Painter @Composable get() = painterResource(Res.drawable.ic_home)
    val Person: Painter @Composable get() = painterResource(Res.drawable.ic_person)
    val Settings: Painter @Composable get() = painterResource(Res.drawable.ic_settings)
}
