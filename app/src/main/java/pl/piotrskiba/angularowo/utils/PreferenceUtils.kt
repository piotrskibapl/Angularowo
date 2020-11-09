package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R

class PreferenceUtils(private val context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    var accessToken: String?
        get() = sharedPreferences.getString(context.getString(R.string.pref_key_access_token), null)
        set(value) {
            sharedPreferences.edit().apply {
                putString(context.getString(R.string.pref_key_access_token), value)
                apply()
            }
        }

    var uuid: String?
        get() = sharedPreferences.getString(context.getString(R.string.pref_key_uuid), null)
        set(value) {
            sharedPreferences.edit().apply {
                putString(context.getString(R.string.pref_key_uuid), value)
                apply()
            }
        }

    var username: String?
        get() = sharedPreferences.getString(context.getString(R.string.pref_key_nickname), null)
        set(value) {
            sharedPreferences.edit().apply {
                putString(context.getString(R.string.pref_key_nickname), value)
                apply()
            }
        }

    var rankName: String?
        get() = sharedPreferences.getString(context.getString(R.string.pref_key_rank), null)
        set(value) {
            sharedPreferences.edit().apply {
                putString(context.getString(R.string.pref_key_rank), value)
                apply()
            }
        }

    var balance: Float
        get() = sharedPreferences.getFloat(context.getString(R.string.pref_key_balance), 0f)
        set(value) {
            sharedPreferences.edit().apply {
                putFloat(context.getString(R.string.pref_key_balance), value)
                apply()
            }
        }

    var tokens: Int
        get() = sharedPreferences.getInt(context.getString(R.string.pref_key_tokens), 0)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(context.getString(R.string.pref_key_tokens), value)
                apply()
            }
        }

    var playtime: Int
        get() = sharedPreferences.getInt(context.getString(R.string.pref_key_playtime), 0)
        set(value) {
            sharedPreferences.edit().apply {
                putInt(context.getString(R.string.pref_key_playtime), value)
                apply()
            }
        }

    var hasSeenFavoriteShowcase: Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.pref_key_showcase_favorite), false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(context.getString(R.string.pref_key_showcase_favorite), value)
                apply()
            }
        }

    var subscribedToFirebaseUuidTopic: Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.pref_key_subscribed_to_uuid_topic), false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(context.getString(R.string.pref_key_subscribed_to_uuid_topic), value)
                apply()
            }
        }

    var subscribedToFirebaseEventsTopic: Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.pref_key_subscribed_to_events), false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(context.getString(R.string.pref_key_subscribed_to_events), value)
                apply()
            }
        }

    var subscribedToFirebasePrivateMessagesTopic: Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.pref_key_subscribed_to_private_messages), false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(context.getString(R.string.pref_key_subscribed_to_private_messages), value)
                apply()
            }
        }

    var subscribedToFirebaseAccountIncidentsTopic: Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.pref_key_subscribed_to_account_incidents), false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(context.getString(R.string.pref_key_subscribed_to_account_incidents), value)
                apply()
            }
        }

    var subscribedToFirebaseNewReportsTopic: Boolean
        get() = sharedPreferences.getBoolean(context.getString(R.string.pref_key_subscribed_to_new_reports), false)
        set(value) {
            sharedPreferences.edit().apply {
                putBoolean(context.getString(R.string.pref_key_subscribed_to_new_reports), value)
                apply()
            }
        }

    fun clearUserData() {
        sharedPreferences.edit().apply {
            remove(context.getString(R.string.pref_key_access_token))
            remove(context.getString(R.string.pref_key_uuid))
            remove(context.getString(R.string.pref_key_nickname))
            remove(context.getString(R.string.pref_key_rank))
            remove(context.getString(R.string.pref_key_subscribed_to_uuid_topic))
        }
    }
}