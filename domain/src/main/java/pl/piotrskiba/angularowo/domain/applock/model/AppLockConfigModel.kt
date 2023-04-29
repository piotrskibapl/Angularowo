package pl.piotrskiba.angularowo.domain.applock.model

data class AppLockConfigModel(
    val title: String,
    val body: String,
    val startTimestamp: Long,
    val endTimestamp: Long,
)
