package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.core.content.ContextCompat
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import java.util.Date
import java.util.concurrent.TimeUnit

object TextUtils {

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

    @JvmStatic
    fun replaceColorCodes(context: Context, string: String): String {
        var s = string

        s = s.replace(
            "§0",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_0)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§1",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_1)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§2",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_2)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§3",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_3)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§4",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_4)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§5",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_5)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§6",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_6)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§7",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_7)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§8",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_8)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§9",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_9)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§a",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_a)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§b",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_b)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§c",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_c)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§d",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_d)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§e",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_e)).substring(2, 8) + ">",
        )
        s = s.replace(
            "§f",
            "</font><font color=#" + Integer.toHexString(ContextCompat.getColor(context, R.color.color_minecraft_f)).substring(2, 8) + ">",
        )

        return s
    }

    fun replaceQualifiers(context: Context, s: String): String {
        return if (s.contains(Constants.USERNAME_QUALIFIER)) {
            val username = PreferenceUtils(context).username

            if (username != null) {
                s.replace(Constants.USERNAME_QUALIFIER, username)
            } else {
                s
            }
        } else {
            s
        }
    }
}
