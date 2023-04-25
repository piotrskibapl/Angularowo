package pl.piotrskiba.angularowo.data.base.preferences.repository

import android.content.SharedPreferences
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository

private const val PREF_KEY_ACCESS_TOKEN = "access_token"
private const val PREF_KEY_UUID = "uuid"
private const val PREF_KEY_USERNAME = "nickname"
private const val PREF_KEY_RANK_NAME = "rank"
private const val PREF_KEY_FAVORITE_SHOWCASE_SHOWN = "showcase_favorite_shown"
private const val PREF_KEY_FIREBASE_SUBSCRIBED_APP_VERSION = "subscribed_app_version"
private const val PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_UUID = "subscribed_player_uuid"
private const val PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_RANK_NAME = "subscribed_player_rank_name"
private const val PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED = "events_subscribed"
private const val PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED = "private_messages_subscribed"
private const val PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED = "account_incidents_subscribed"
private const val PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED = "new_reports_subscribed"

class PreferencesRepositoryImpl(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {

    override var accessToken: String?
        get() = sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(value) = applyValue(PREF_KEY_ACCESS_TOKEN, value)

    override var uuid: String?
        get() = sharedPreferences.getString(PREF_KEY_UUID, null)
        set(value) = applyValue(PREF_KEY_UUID, value)

    override var username: String?
        get() = sharedPreferences.getString(PREF_KEY_USERNAME, null)
        set(value) = applyValue(PREF_KEY_USERNAME, value)

    override var rankName: String?
        get() = sharedPreferences.getString(PREF_KEY_RANK_NAME, null)
        set(value) = applyValue(PREF_KEY_RANK_NAME, value)

    override var hasSeenFavoriteShowcase: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FAVORITE_SHOWCASE_SHOWN, false)
        set(value) = applyValue(PREF_KEY_FAVORITE_SHOWCASE_SHOWN, value)

    override var subscribedFirebaseAppVersion: Int?
        get() = when (val value =
            sharedPreferences.getInt(PREF_KEY_FIREBASE_SUBSCRIBED_APP_VERSION, -1)) {
            -1 -> null
            else -> value
        }
        set(value) = applyValue(PREF_KEY_FIREBASE_SUBSCRIBED_APP_VERSION, value ?: -1)

    override var subscribedFirebasePlayerUuid: String?
        get() = sharedPreferences.getString(PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_UUID, null)
        set(value) = applyValue(PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_UUID, value)

    override var subscribedFirebasePlayerRankName: String?
        get() = sharedPreferences.getString(PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_RANK_NAME, null)
        set(value) = applyValue(PREF_KEY_FIREBASE_SUBSCRIBED_PLAYER_RANK_NAME, value)

    override var subscribedToFirebaseEventsTopic: Boolean?
        get() = if (sharedPreferences.contains(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED)) {
            sharedPreferences.getBoolean(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED, false)
        } else {
            null
        }
        set(value) = if (value != null) {
            applyValue(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED, value)
        } else {
            deleteValue(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED)
        }

    override var subscribedToFirebasePrivateMessagesTopic: Boolean?
        get() = if (sharedPreferences.contains(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED)) {
            sharedPreferences.getBoolean(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED, false)
        } else {
            null
        }
        set(value) = if (value != null) {
            applyValue(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED, value)
        } else {
            deleteValue(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED)
        }

    override var subscribedToFirebaseAccountIncidentsTopic: Boolean?
        get() = if (sharedPreferences.contains(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED)) {
            sharedPreferences.getBoolean(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED, false)
        } else {
            null
        }
        set(value) = if (value != null) {
            applyValue(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED, value)
        } else {
            deleteValue(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED)
        }

    override var subscribedToFirebaseNewReportsTopic: Boolean?
        get() = if (sharedPreferences.contains(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED)) {
            sharedPreferences.getBoolean(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED, false)
        } else {
            null
        }
        set(value) = if (value != null) {
            applyValue(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED, value)
        } else {
            deleteValue(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED)
        }

    override fun clearUserData() {
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

    private fun deleteValue(key: String) {
        sharedPreferences.edit().apply {
            remove(key)
            apply()
        }
    }
}
