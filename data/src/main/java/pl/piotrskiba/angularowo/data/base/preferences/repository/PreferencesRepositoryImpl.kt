package pl.piotrskiba.angularowo.data.base.preferences.repository

import android.content.SharedPreferences
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository

private const val PREF_KEY_ACCESS_TOKEN = "access_token"
private const val PREF_KEY_UUID = "uuid"
private const val PREF_KEY_USERNAME = "nickname"
private const val PREF_KEY_RANK_NAME = "rank"
private const val PREF_KEY_FAVORITE_SHOWCASE_SHOWN = "showcase_favorite_shown"
private const val PREF_KEY_FIREBASE_SUBSCRIBED_APP_VERSION = "subscribed_app_version"
private const val PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_RANK_NAME = "subscribed_player_rank_name"
private const val PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED = "events_subscribed"
private const val PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED = "private_messages_subscribed"
private const val PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED = "account_incidents_subscribed"
private const val PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED = "new_reports_subscribed"

class PreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {

    override fun accessToken(): Maybe<String> =
        sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null).let { accessToken ->
            if (accessToken == null) {
                Maybe.empty()
            } else {
                Maybe.just(accessToken)
            }
        }

    override fun setAccessToken(accessToken: String): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_ACCESS_TOKEN, accessToken)
        }

    override fun uuid(): Maybe<String> =
        sharedPreferences.getString(PREF_KEY_UUID, null).let { uuid ->
            if (uuid == null) {
                Maybe.empty()
            } else {
                Maybe.just(uuid)
            }
        }

    override fun setUuid(uuid: String): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_UUID, uuid)
        }

    override fun username(): Maybe<String> =
        sharedPreferences.getString(PREF_KEY_USERNAME, null).let { username ->
            if (username == null) {
                Maybe.empty()
            } else {
                Maybe.just(username)
            }
        }

    override fun setUsername(username: String): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_USERNAME, username)
        }

    override fun rankName(): Maybe<String> =
        sharedPreferences.getString(PREF_KEY_RANK_NAME, null).let { rankName ->
            if (rankName == null) {
                Maybe.empty()
            } else {
                Maybe.just(rankName)
            }
        }

    override fun setRankName(rankName: String): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_RANK_NAME, rankName)
        }

    override fun hasSeenFavoriteShowcase(): Single<Boolean> =
        Single.fromCallable {
            sharedPreferences.getBoolean(PREF_KEY_FAVORITE_SHOWCASE_SHOWN, false)
        }

    override fun setHasSeenFavoriteShowcase(hasSeenFavoriteShowcase: Boolean): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FAVORITE_SHOWCASE_SHOWN, hasSeenFavoriteShowcase)
        }

    override fun subscribedFirebaseAppVersion(): Maybe<Int> =
        sharedPreferences.getInt(PREF_KEY_FIREBASE_SUBSCRIBED_APP_VERSION, -1).let { version ->
            if (version == -1) {
                Maybe.empty()
            } else {
                Maybe.just(version)
            }
        }

    override fun setSubscribedFirebaseAppVersion(subscribedFirebaseAppVersion: Int): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FIREBASE_SUBSCRIBED_APP_VERSION, subscribedFirebaseAppVersion)
        }

    override fun subscribedFirebasePlayerRankName(): Maybe<String> =
        sharedPreferences.getString(PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_RANK_NAME, null).let { rankName ->
            if (rankName == null) {
                Maybe.empty()
            } else {
                Maybe.just(rankName)
            }
        }

    override fun setSubscribedFirebasePlayerRankName(subscribedFirebasePlayerRankName: Int): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_RANK_NAME, subscribedFirebasePlayerRankName)
        }

    override fun subscribedToFirebaseEventsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseEventsTopic(subscribedToFirebaseEventsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED, subscribedToFirebaseEventsTopic)
        }

    override fun subscribedToFirebasePrivateMessagesTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebasePrivateMessagesTopic(subscribedToFirebasePrivateMessagesTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED, subscribedToFirebasePrivateMessagesTopic)
        }

    override fun subscribedToFirebaseAccountIncidentsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseAccountIncidentsTopic(subscribedToFirebaseAccountIncidentsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED, subscribedToFirebaseAccountIncidentsTopic)
        }

    override fun subscribedToFirebaseNewReportsTopic(): Maybe<Boolean> =
        if (sharedPreferences.contains(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED)) {
            Maybe.just(sharedPreferences.getBoolean(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED, false))
        } else {
            Maybe.empty()
        }

    override fun setSubscribedToFirebaseNewReportsTopic(subscribedToFirebaseNewReportsTopic: Boolean): Completable =
        Completable.fromAction {
            applyValue(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED, subscribedToFirebaseNewReportsTopic)
        }

    override fun clearUserData(): Completable =
        Completable.fromAction {
            sharedPreferences.edit().apply {
                remove(PREF_KEY_ACCESS_TOKEN)
                remove(PREF_KEY_UUID)
                remove(PREF_KEY_USERNAME)
                remove(PREF_KEY_RANK_NAME)
                apply()
            }
        }

    private fun applyValue(key: String, value: String?) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    private fun applyValue(key: String, value: Int) {
        sharedPreferences.edit().apply {
            putInt(key, value)
            apply()
        }
    }

    private fun applyValue(key: String, value: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(key, value)
            apply()
        }
    }
}
