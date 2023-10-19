package pl.piotrskiba.angularowo.applock.model

import pl.piotrskiba.angularowo.domain.applock.model.AppLockDataModel

data class AppLockData(
    val title: String,
    val body: String,
    val canSkip: Boolean,
)

fun AppLockDataModel.toUi() =
    AppLockData(
        title = config.title.replace("\\n", "\n"),
        body = config.body.replace("\\n", "\n"),
        canSkip = canSkip,
    )
