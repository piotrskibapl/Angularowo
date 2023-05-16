package pl.piotrskiba.angularowo.domain.base.preferences.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single

interface PreferencesRepository {

    fun accessToken(): Maybe<String>
    fun setAccessToken(accessToken: String): Completable
    fun uuid(): Maybe<String>
    fun setUuid(uuid: String): Completable
    fun username(): Maybe<String>
    fun setUsername(username: String): Completable
    fun rankName(): Maybe<String>
    fun setRankName(rankName: String): Completable
    fun hasSeenFavoriteShowcase(): Single<Boolean>
    fun setHasSeenFavoriteShowcase(hasSeenFavoriteShowcase: Boolean): Completable
    fun subscribedFirebaseAppVersion(): Maybe<Int>
    fun setSubscribedFirebaseAppVersion(subscribedFirebaseAppVersion: Int): Completable
    fun subscribedFirebasePlayerRankName(): Maybe<String>
    fun setSubscribedFirebasePlayerRankName(subscribedFirebasePlayerRankName: Int): Completable
    var subscribedToFirebaseEventsTopic: Boolean? // TODO: use enum class
    var subscribedToFirebasePrivateMessagesTopic: Boolean? // TODO: use enum class
    var subscribedToFirebaseAccountIncidentsTopic: Boolean? // TODO: use enum class
    var subscribedToFirebaseNewReportsTopic: Boolean? // TODO: use enum class
    fun clearUserData(): Completable
}
