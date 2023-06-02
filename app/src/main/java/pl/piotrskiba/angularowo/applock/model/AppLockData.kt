package pl.piotrskiba.angularowo.applock.model

import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel

data class AppLockData(
    val title: String,
    val body: String,
)

fun AppLockConfigModel.toUi() =
    AppLockData(
        title = title.replace("\\n", "\n"),
        body = body.replace("\\n", "\n"),
    )
