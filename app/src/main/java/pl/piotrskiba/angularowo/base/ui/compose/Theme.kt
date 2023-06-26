package pl.piotrskiba.angularowo.base.ui.compose

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import pl.piotrskiba.angularowo.R

fun ComposeView.setThemedContent(content: @Composable () -> Unit) {
    setContent {
        Theme(content)
    }
}

@Composable
private fun Theme(content: @Composable () -> Unit) {
    val colors = darkColors(
        primary = colorResource(id = R.color.colorPrimary),
        primaryVariant = colorResource(id = R.color.colorPrimaryDark),
        secondary = colorResource(id = R.color.colorAccent),
        background = colorResource(id = R.color.windowBackground),
    )
    val typography = Typography(
        h1 = TextStyle(
            color = colorResource(id = R.color.secondary_text),
            fontSize = 24.sp,
        ),
        h2 = TextStyle(
            color = colorResource(id = R.color.secondary_text),
            fontSize = 22.sp,
        ),
        h3 = TextStyle(
            color = colorResource(id = R.color.secondary_text),
            fontSize = 20.sp,
        ),
        h6 = TextStyle(
            color = colorResource(id = R.color.secondary_text),
            fontSize = 16.sp,
        ),
        body1 = TextStyle(
            color = colorResource(id = R.color.secondary_text),
            fontSize = 14.sp,
        ),
        body2 = TextStyle(
            color = colorResource(id = R.color.secondary_text),
            fontSize = 12.sp,
        ),
    )
    MaterialTheme(
        colors = colors,
        typography = typography,
        content = content,
    )
}
