package pl.piotrskiba.angularowo.domain.base.preferences.repository

interface PreferencesRepository {

    var accessToken: String?
    var uuid: String?
    var skinUuid: String?
    var username: String?
    var rankName: String?
    var balance: Float
    var tokens: Int
    var playtime: Int
    var hasSeenFavoriteShowcase: Boolean
    var subscribedFirebaseAppVersion: Int?
    var subscribedFirebasePlayerUuid: String?
    var subscribedToFirebaseEventsTopic: Boolean
    var subscribedToFirebasePrivateMessagesTopic: Boolean
    var subscribedToFirebaseAccountIncidentsTopic: Boolean
    var subscribedToFirebaseNewReportsTopic: Boolean
    fun clearUserData()
}
