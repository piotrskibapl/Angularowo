package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R

class PreferenceUtils(private val context: Context) {

    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

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
}