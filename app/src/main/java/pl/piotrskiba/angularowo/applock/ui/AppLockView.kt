package pl.piotrskiba.angularowo.applock.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.piotrskiba.angularowo.applock.model.AppLockData
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.ui.compose.AngularowoCircularProgressIndicator

@Composable
fun AppLockView(data: AppLockData?, state: ViewModelState) {
    if (state.fullscreenLoaderVisible() || data == null) {
        AngularowoCircularProgressIndicator()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text( // TODO: setup typography
                modifier = Modifier.padding(8.dp, 60.dp, 8.dp, 8.dp),
                text = data.title,
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
            )
            Text(
                modifier = Modifier.padding(8.dp, 24.dp, 8.dp, 8.dp),
                text = data.body,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Preview
@Composable
fun AppLockViewPreview() {
    AppLockView(
        data = AppLockData(
            title = "Title",
            body = "Body",
        ),
        state = ViewModelState.Loaded,
    )
}
