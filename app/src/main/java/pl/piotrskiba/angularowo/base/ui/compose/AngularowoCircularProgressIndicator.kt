package pl.piotrskiba.angularowo.base.ui.compose

import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import pl.piotrskiba.angularowo.R

@Composable
fun AngularowoCircularProgressIndicator() {
    CircularProgressIndicator(
        modifier = Modifier.wrapContentSize(),
        color = colorResource(id = R.color.colorAccent),
    )
}

@Preview
@Composable
fun AngularowoCircularProgressIndicatorPreview() {
    AngularowoCircularProgressIndicator()
}
