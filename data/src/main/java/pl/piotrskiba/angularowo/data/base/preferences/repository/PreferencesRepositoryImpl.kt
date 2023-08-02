package pl.piotrskiba.angularowo.data.base.preferences.repository

import android.annotation.SuppressLint
import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository

@SuppressLint("ApplySharedPref")
class PreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : PreferencesRepository {

    override fun accessToken(): Maybe<String> =
        sharedPreferences.getString(PrefKey.accessToken, null).let { accessToken ->
            if (accessToken == null) {
                Maybe.empty()
            } else {
                Maybe.just(accessToken)
            }
        }

    override fun setAccessToken(accessToken: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.accessToken, accessToken)
        }

    override fun uuid(): Maybe<String> =
        sharedPreferences.getString(PrefKey.uuid, null).let { uuid ->
            if (uuid == null) {
                Maybe.empty()
            } else {
                Maybe.just(uuid)
            }
        }

    override fun setUuid(uuid: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.uuid, uuid)
        }

    override fun username(): Maybe<String> =
        sharedPreferences.getString(PrefKey.username, null).let { username ->
            if (username == null) {
                Maybe.empty()
            } else {
                Maybe.just(username)
            }
        }

    override fun setUsername(username: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.username, username)
        }

    override fun rankName(): Maybe<String> =
        sharedPreferences.getString(PrefKey.rankName, null).let { rankName ->
            if (rankName == null) {
                Maybe.empty()
            } else {
                Maybe.just(rankName)
            }
        }

    override fun setRankName(rankName: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.rankName, rankName)
        }

    override fun hasSeenFavoriteShowcase(): Single<Boolean> =
        Single.fromCallable {
            sharedPreferences.getBoolean(PrefKey.favoriteShowcaseShown, false)
        }

    override fun setHasSeenFavoriteShowcase(hasSeenFavoriteShowcase: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.favoriteShowcaseShown, hasSeenFavoriteShowcase)
        }

    override fun subscribedFirebaseAppVersion(): Maybe<Int> =
        sharedPreferences.getInt(PrefKey.fcmSubscribedAppVersion, -1).let { version ->
            if (version == -1) {
                Maybe.empty()
            } else {
                Maybe.just(version)
            }
        }

    override fun setSubscribedFirebaseAppVersion(subscribedFirebaseAppVersion: Int): Completable =
        Completable.fromAction {
            applyValue(PrefKey.fcmSubscribedAppVersion, subscribedFirebaseAppVersion)
        }

    override fun subscribedFirebasePlayerRankName(): Maybe<String> =
        sharedPreferences.getString(PrefKey.fcmSubscribedPlayerRankName, null).let { rankName ->
            if (rankName == null) {
                Maybe.empty()
            } else {
                Maybe.just(rankName)
            }
        }

    override fun setSubscribedFirebasePlayerRankName(subscribedFirebasePlayerRankName: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.fcmSubscribedPlayerRankName, subscribedFirebasePlayerRankName)
        }

    override fun subscribedToFirebaseEventsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.fcmEventsSubscribed)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.fcmEventsSubscribed, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseEventsTopic(subscribedToFirebaseEventsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.fcmEventsSubscribed, subscribedToFirebaseEventsTopic)
        }

    override fun subscribedToFirebasePrivateMessagesTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.fcmPrivateMessagesSubscribed)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.fcmPrivateMessagesSubscribed, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebasePrivateMessagesTopic(subscribedToFirebasePrivateMessagesTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.fcmPrivateMessagesSubscribed, subscribedToFirebasePrivateMessagesTopic)
        }

    override fun subscribedToFirebaseAccountIncidentsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.fcmAccountIncidentsSubscribed)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.fcmAccountIncidentsSubscribed, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseAccountIncidentsTopic(subscribedToFirebaseAccountIncidentsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.fcmAccountIncidentsSubscribed, subscribedToFirebaseAccountIncidentsTopic)
        }

    override fun subscribedToFirebaseNewReportsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.fcmNewReportsSubscribed)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.fcmNewReportsSubscribed, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseNewReportsTopic(subscribedToFirebaseNewReportsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.fcmNewReportsSubscribed, subscribedToFirebaseNewReportsTopic)
        }

    override fun clearUserData(): Completable =
        Completable.fromAction {
            sharedPreferences.edit().apply {
                remove(PrefKey.accessToken)
                remove(PrefKey.uuid)
                remove(PrefKey.username)
                remove(PrefKey.rankName)
                commit()
            }
        }

    private fun applyValue(key: String, value: String?) {
        sharedPreferences.edit().apply {
            putString(key, value)
            commit()
        }
    }

    private fun applyValue(key: String, value: Int) {
        sharedPreferences.edit().apply {
            putInt(key, value)
            commit()
        }
    }

    private fun applyValue(key: String, value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(key, value)
            commit()
        }
    }

    private object PrefKey {
        const val accessToken = "access_token"
        const val uuid = "uuid"
        const val username = "nickname"
        const val rankName = "rank"
        const val favoriteShowcaseShown = "showcase_favorite_shown"
        const val fcmSubscribedAppVersion = "subscribed_app_version"
        const val fcmSubscribedPlayerRankName = "subscribed_player_rank_name"
        const val fcmEventsSubscribed = "events_subscribed"
        const val fcmPrivateMessagesSubscribed = "private_messages_subscribed"
        const val fcmAccountIncidentsSubscribed = "account_incidents_subscribed"
        const val fcmNewReportsSubscribed = "new_reports_subscribed"
    }
}
