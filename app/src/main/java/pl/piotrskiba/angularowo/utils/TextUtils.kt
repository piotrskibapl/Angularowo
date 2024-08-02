package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R
import java.util.Date
import java.util.concurrent.TimeUnit

// TODO: should be dropped
object TextUtils {

    private const val USERNAME_QUALIFIER = "%player%"

    @JvmStatic
    fun formatPlaytime(context: Context, playtime: Long): String {
        var remainingSeconds = playtime / 1000
        var days = 0
        var hours = 0
        var minutes = 0

        while (remainingSeconds >= 60 * 60 * 24) {
            days++
            remainingSeconds -= 60 * 60 * 24
        }
        while (remainingSeconds >= 60 * 60) {
            hours++
            remainingSeconds -= 60 * 60
        }
        while (remainingSeconds >= 60) {
            minutes++
            remainingSeconds -= 60
        }

        var result = ""
        if (days > 0) {
            result = context.resources.getQuantityString(R.plurals.days, days, days)
        }
        if (hours > 0) {
            when {
                result.isEmpty() -> {
                    result = context.resources.getQuantityString(R.plurals.hours, hours, hours)
                }

                minutes > 0 -> {
                    result += ", " + context.resources.getQuantityString(R.plurals.hours, hours, hours)
                }

                else -> {
                    result += " i " + context.resources.getQuantityString(R.plurals.hours, hours, hours)
                }
            }
        }
        if (minutes > 0) {
            if (result.isEmpty()) {
                result = context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)
            } else {
                result += " i " + context.resources.getQuantityString(R.plurals.minutes, minutes, minutes)
            }
        }

        return result
    }

    @JvmStatic
    fun formatTimeDifference(from: Date, to: Date): String {
        var remainingMilliseconds = from.time - to.time
        val days = remainingMilliseconds / TimeUnit.DAYS.toMillis(1)
        remainingMilliseconds %= TimeUnit.DAYS.toMillis(1)
        val hours = remainingMilliseconds / TimeUnit.HOURS.toMillis(1)
        remainingMilliseconds %= TimeUnit.HOURS.toMillis(1)
        val minutes = remainingMilliseconds / TimeUnit.MINUTES.toMillis(1)
        remainingMilliseconds %= TimeUnit.MINUTES.toMillis(1)
        val seconds = remainingMilliseconds / TimeUnit.SECONDS.toMillis(1)
        remainingMilliseconds %= TimeUnit.SECONDS.toMillis(1)

        val builder = StringBuilder()
        if (days > 0) builder.append(days).append(" d")
        if (hours > 0) {
            if (builder.isNotEmpty()) builder.append(" ")
            builder.append(hours).append(" h")
        }
        if (minutes > 0 && days == 0L) {
            if (builder.isNotEmpty()) builder.append(" ")
            builder.append(minutes).append(" m")
        }
        if (seconds > 0 && days == 0L && hours == 0L) {
            if (builder.isNotEmpty()) builder.append(" ")
            builder.append(seconds).append(" s")
        }
        return builder.toString()
    }

    fun replaceQualifiers(context: Context, s: String): String {
        return if (s.contains(USERNAME_QUALIFIER)) {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val username = preferences.getString(context.getString(R.string.pref_key_nickname), null)
            if (username != null) {
                s.replace(USERNAME_QUALIFIER, username)
            } else {
                s
            }
        } else {
            s
        }
    }
}
