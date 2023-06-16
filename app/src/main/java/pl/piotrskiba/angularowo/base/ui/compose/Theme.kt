package pl.piotrskiba.angularowo.base.ui.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import pl.piotrskiba.angularowo.R

fun ComposeView.setThemedContent(content: @Composable () -> Unit) {
    setContent {
        Theme(content)
    }
}

@Composable
private fun Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colors = darkColors(
            primary = colorResource(id = R.color.colorPrimary),
            primaryVariant = colorResource(id = R.color.colorPrimaryDark),
            secondary = colorResource(id = R.color.colorAccent),
            background = colorResource(id = R.color.windowBackground),
        ),
        content = {
            ProvideTextStyle(
                value = TextStyle(color = colorResource(id = R.color.secondary_text)),
                content = content,
            )
        },
    )
}
