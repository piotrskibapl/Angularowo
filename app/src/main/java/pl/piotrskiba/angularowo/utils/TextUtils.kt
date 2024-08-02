package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R
import java.util.concurrent.TimeUnit

// TODO: should be dropped
object TextUtils {

    private const val USERNAME_QUALIFIER = "%player%"

    @JvmStatic
    fun formatPlaytime(context: Context, playtime: Long): String {
        val millis = playtime
        val days = (millis / TimeUnit.DAYS.toMillis(1)).toInt()
        val hours = (millis / TimeUnit.HOURS.toMillis(1) % 24).toInt()
        val minutes = (millis / TimeUnit.MINUTES.toMillis(1) % 60).toInt()

        val result = StringBuilder()
        with(context.resources) {
            if (days > 0) result.append(getQuantityString(R.plurals.days, days, days))
            if (hours > 0) {
                when {
                    result.isEmpty() -> result.append(getQuantityString(R.plurals.hours, hours, hours))
                    minutes > 0 -> result.append(", ${getQuantityString(R.plurals.hours, hours, hours)}")
                    else -> result.append(" ${getString(R.string.and)} ${getQuantityString(R.plurals.hours, hours, hours)}")
                }
            }
            if (minutes > 0) {
                if (result.isEmpty()) {
                    result.append(getQuantityString(R.plurals.minutes, minutes, minutes))
                } else {
                    result.append(" ${getString(R.string.and)} ${getQuantityString(R.plurals.minutes, minutes, minutes)}")
                }
            }
        }
        return result.toString()
    }

    fun replaceQualifiers(context: Context, s: String): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val username = preferences.getString(context.getString(R.string.pref_key_nickname), null)
        if (s.contains(USERNAME_QUALIFIER) && username != null) {
            return s.replace(USERNAME_QUALIFIER, username)
        }
        return s
    }
}
