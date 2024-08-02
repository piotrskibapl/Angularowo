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
        sharedPreferences.getString(PrefKey.ACCESS_TOKEN, null).let { accessToken ->
            if (accessToken == null) {
                Maybe.empty()
            } else {
                Maybe.just(accessToken)
            }
        }

    override fun setAccessToken(accessToken: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.ACCESS_TOKEN, accessToken)
        }

    override fun uuid(): Maybe<String> =
        sharedPreferences.getString(PrefKey.UUID, null).let { uuid ->
            if (uuid == null) {
                Maybe.empty()
            } else {
                Maybe.just(uuid)
            }
        }

    override fun setUuid(uuid: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.UUID, uuid)
        }

    override fun username(): Maybe<String> =
        sharedPreferences.getString(PrefKey.USERNAME, null).let { username ->
            if (username == null) {
                Maybe.empty()
            } else {
                Maybe.just(username)
            }
        }

    override fun setUsername(username: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.USERNAME, username)
        }

    override fun rankName(): Maybe<String> =
        sharedPreferences.getString(PrefKey.RANK_NAME, null).let { rankName ->
            if (rankName == null) {
                Maybe.empty()
            } else {
                Maybe.just(rankName)
            }
        }

    override fun setRankName(rankName: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.RANK_NAME, rankName)
        }

    override fun hasSeenFavoriteShowcase(): Single<Boolean> =
        Single.fromCallable {
            sharedPreferences.getBoolean(PrefKey.FAVORITE_SHOWCASE_SHOWN, false)
        }

    override fun setHasSeenFavoriteShowcase(hasSeenFavoriteShowcase: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FAVORITE_SHOWCASE_SHOWN, hasSeenFavoriteShowcase)
        }

    override fun subscribedFirebaseAppVersion(): Maybe<Int> =
        sharedPreferences.getInt(PrefKey.FCM_SUBSCRIBED_APP_VERSION, -1).let { version ->
            if (version == -1) {
                Maybe.empty()
            } else {
                Maybe.just(version)
            }
        }

    override fun setSubscribedFirebaseAppVersion(subscribedFirebaseAppVersion: Int): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FCM_SUBSCRIBED_APP_VERSION, subscribedFirebaseAppVersion)
        }

    override fun subscribedFirebasePlayerRankName(): Maybe<String> =
        sharedPreferences.getString(PrefKey.FCM_SUBSCRIBED_PLAYER_RANK_NAME, null).let { rankName ->
            if (rankName == null) {
                Maybe.empty()
            } else {
                Maybe.just(rankName)
            }
        }

    override fun setSubscribedFirebasePlayerRankName(subscribedFirebasePlayerRankName: String): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FCM_SUBSCRIBED_PLAYER_RANK_NAME, subscribedFirebasePlayerRankName)
        }

    override fun subscribedToFirebaseEventsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.FCM_EVENTS_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.FCM_EVENTS_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseEventsTopic(subscribedToFirebaseEventsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FCM_EVENTS_SUBSCRIBED, subscribedToFirebaseEventsTopic)
        }

    override fun subscribedToFirebasePrivateMessagesTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.FCM_PRIVATE_MESSAGES_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.FCM_PRIVATE_MESSAGES_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebasePrivateMessagesTopic(subscribedToFirebasePrivateMessagesTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FCM_PRIVATE_MESSAGES_SUBSCRIBED, subscribedToFirebasePrivateMessagesTopic)
        }

    override fun subscribedToFirebaseAccountIncidentsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.FCM_ACCOUNT_INCIDENTS_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.FCM_ACCOUNT_INCIDENTS_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseAccountIncidentsTopic(subscribedToFirebaseAccountIncidentsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FCM_ACCOUNT_INCIDENTS_SUBSCRIBED, subscribedToFirebaseAccountIncidentsTopic)
        }

    override fun subscribedToFirebaseNewReportsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PrefKey.FCM_NEW_REPORTS_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PrefKey.FCM_NEW_REPORTS_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseNewReportsTopic(subscribedToFirebaseNewReportsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PrefKey.FCM_NEW_REPORTS_SUBSCRIBED, subscribedToFirebaseNewReportsTopic)
        }

    override fun clearUserData(): Completable =
        Completable.fromAction {
            sharedPreferences.edit().apply {
                remove(PrefKey.ACCESS_TOKEN)
                remove(PrefKey.UUID)
                remove(PrefKey.USERNAME)
                remove(PrefKey.RANK_NAME)
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
        const val ACCESS_TOKEN = "access_token"
        const val UUID = "uuid"
        const val USERNAME = "nickname"
        const val RANK_NAME = "rank"
        const val FAVORITE_SHOWCASE_SHOWN = "showcase_favorite_shown"
        const val FCM_SUBSCRIBED_APP_VERSION = "subscribed_app_version"
        const val FCM_SUBSCRIBED_PLAYER_RANK_NAME = "subscribed_player_rank_name"
        const val FCM_EVENTS_SUBSCRIBED = "events_subscribed"
        const val FCM_PRIVATE_MESSAGES_SUBSCRIBED = "private_messages_subscribed"
        const val FCM_ACCOUNT_INCIDENTS_SUBSCRIBED = "account_incidents_subscribed"
        const val FCM_NEW_REPORTS_SUBSCRIBED = "new_reports_subscribed"
    }
}
