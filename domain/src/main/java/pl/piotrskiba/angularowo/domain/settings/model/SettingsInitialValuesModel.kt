package pl.piotrskiba.angularowo.domain.settings.model

data class SettingsInitialValuesModel(
    val subscribedToEvents: Boolean,
    val subscribedToPrivateMessages: Boolean,
    val subscribedToAccountIncidents: Boolean,
    val subscribedToNewReports: Boolean,
    val newReportsVisible: Boolean,
)
