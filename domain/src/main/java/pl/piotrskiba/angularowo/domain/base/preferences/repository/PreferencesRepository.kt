package pl.piotrskiba.angularowo.domain.base.preferences.repository

interface PreferencesRepository {

    var accessToken: String?
    var uuid: String?
    var username: String?
    var rankName: String?
    var hasSeenFavoriteShowcase: Boolean
    var subscribedFirebaseAppVersion: Int?
    var subscribedFirebasePlayerUuid: String?
    var subscribedFirebasePlayerRankName: String?
    var subscribedToFirebaseEventsTopic: Boolean? // TODO: use enum class
    var subscribedToFirebasePrivateMessagesTopic: Boolean? // TODO: use enum class
    var subscribedToFirebaseAccountIncidentsTopic: Boolean? // TODO: use enum class
    var subscribedToFirebaseNewReportsTopic: Boolean? // TODO: use enum class
    fun clearUserData()
}
