package pl.piotrskiba.angularowo.domain.base.preferences.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe

interface PreferencesRepository {

    fun accessToken(): Maybe<String>
    fun setAccessToken(accessToken: String): Completable
    fun uuid(): Maybe<String>
    fun setUuid(uuid: String): Completable
    fun username(): Maybe<String>
    fun setUsername(username: String): Completable
    fun rankName(): Maybe<String>
    fun setRankName(rankName: String): Completable
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
