package pl.piotrskiba.angularowo.domain.applock.model

data class AppLockDataModel(
    val config: AppLockConfigModel,
    val canSkip: Boolean,
)
