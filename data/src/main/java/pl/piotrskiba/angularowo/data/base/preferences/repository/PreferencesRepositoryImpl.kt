package pl.piotrskiba.angularowo.data.base.preferences.repository

import android.content.SharedPreferences
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import javax.inject.Inject

private const val PREF_KEY_ACCESS_TOKEN = "access_token"
private const val PREF_KEY_UUID = "uuid"
private const val PREF_KEY_SKIN_UUID = "skin_uuid"
private const val PREF_KEY_USERNAME = "nickname"
private const val PREF_KEY_RANK_NAME = "rank"
private const val PREF_KEY_BALANCE = "balance"
private const val PREF_KEY_TOKENS = "tokens"
private const val PREF_KEY_PLAYTIME = "playtime"
private const val PREF_KEY_FAVORITE_SHOWCASE_SHOWN = "showcase_favorite_shown"
private const val PREF_KEY_FIREBASE_UUID_SUBSCRIBED = "uuid_topic_subscribed"
private const val PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED = "events_subscribed"
private const val PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED = "private_messages_subscribed"
private const val PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED = "account_incidents_subscribed"
private const val PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED = "new_reports_subscribed"

class PreferencesRepositoryImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : PreferencesRepository {

    override var accessToken: String?
        get() = sharedPreferences.getString(PREF_KEY_ACCESS_TOKEN, null)
        set(value) = applyValue(PREF_KEY_ACCESS_TOKEN, value)

    override var uuid: String?
        get() = sharedPreferences.getString(PREF_KEY_UUID, null)
        set(value) = applyValue(PREF_KEY_UUID, value)

    override var skinUuid: String?
        get() = sharedPreferences.getString(PREF_KEY_SKIN_UUID, null)
        set(value) = applyValue(PREF_KEY_SKIN_UUID, value)

    override var username: String?
        get() = sharedPreferences.getString(PREF_KEY_USERNAME, null)
        set(value) = applyValue(PREF_KEY_USERNAME, value)

    override var rankName: String?
        get() = sharedPreferences.getString(PREF_KEY_RANK_NAME, null)
        set(value) = applyValue(PREF_KEY_RANK_NAME, value)

    override var balance: Float
        get() = sharedPreferences.getFloat(PREF_KEY_BALANCE, 0f)
        set(value) = applyValue(PREF_KEY_BALANCE, value)

    override var tokens: Int
        get() = sharedPreferences.getInt(PREF_KEY_TOKENS, 0)
        set(value) = applyValue(PREF_KEY_TOKENS, value)

    override var playtime: Int
        get() = sharedPreferences.getInt(PREF_KEY_PLAYTIME, 0)
        set(value) = applyValue(PREF_KEY_PLAYTIME, value)

    override var hasSeenFavoriteShowcase: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FAVORITE_SHOWCASE_SHOWN, false)
        set(value) = applyValue(PREF_KEY_FAVORITE_SHOWCASE_SHOWN, value)

    override var subscribedToFirebaseUuidTopic: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FIREBASE_UUID_SUBSCRIBED, false)
        set(value) = applyValue(PREF_KEY_FIREBASE_UUID_SUBSCRIBED, value)

    override var subscribedToFirebaseEventsTopic: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED, false)
        set(value) = applyValue(PREF_KEY_FIREBASE_EVENTS_SUBSCRIBED, value)

    override var subscribedToFirebasePrivateMessagesTopic: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED, false)
        set(value) = applyValue(PREF_KEY_FIREBASE_PRIVATE_MESSAGES_SUBSCRIBED, value)

    override var subscribedToFirebaseAccountIncidentsTopic: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED, false)
        set(value) = applyValue(PREF_KEY_FIREBASE_ACCOUNT_INCIDENTS_SUBSCRIBED, value)

    override var subscribedToFirebaseNewReportsTopic: Boolean
        get() = sharedPreferences.getBoolean(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED, false)
        set(value) = applyValue(PREF_KEY_FIREBASE_NEW_REPORTS_SUBSCRIBED, value)

    override fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(PREF_KEY_ACCESS_TOKEN)
            remove(PREF_KEY_UUID)
            remove(PREF_KEY_USERNAME)
            remove(PREF_KEY_RANK_NAME)
            remove(PREF_KEY_FIREBASE_UUID_SUBSCRIBED)
            apply()
        }
    }

    private fun applyValue(key: String, value: String?) {
        sharedPreferences.edit().apply {
            putString(key, value)
            apply()
        }
    }

    private fun applyValue(key: String, value: Float) {
        sharedPreferences.edit().apply {
            putFloat(key, value)
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