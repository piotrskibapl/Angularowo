package pl.piotrskiba.angularowo.utils

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.main.base.ui.MainActivity
import javax.inject.Inject

class NotificationUtils @Inject constructor(private val context: Context) {

    private object VibrationPattern {
        val DEFAULT = longArrayOf(0, 250, 250, 250)
    }
    private object ChannelId {
        const val DEFAULT = "default_notification_channel"
        const val SILENT = "silent_notification_channel"
    }
    private object Qualifier {
        const val USERNAME = "%player%"
    }

    /**
     * creates all the notification channels for the app
     */
    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            createSilentNotificationChannel()
        }
    }

    /**
     * creates the default (not silent) notification channel
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.notification_channel_name)
            val description = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(ChannelId.DEFAULT, name, importance)
            channel.description = description
            channel.vibrationPattern = VibrationPattern.DEFAULT
            channel.enableVibration(true)

            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val audioAttributes = AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(soundUri, audioAttributes)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * creates the silent notification channel
     */
    private fun createSilentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.notification_channel_name_silent)
            val description = context.getString(R.string.notification_channel_description_silent)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(ChannelId.SILENT, name, importance)
            channel.description = description
            channel.vibrationPattern = longArrayOf(0)
            channel.enableVibration(true)
            channel.setSound(null, null)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    /**
     * A function used to display the notification to the user
     *
     * The function replaces all username qualifiers in the raw message and body with the
     * actual user nickname. It also creates an PendingIntent to open the app when clicked and
     * displays the notification.
     *
     * @param rawTitle notification title
     * @param rawBody notification body
     * @param sound a boolean whether the notification should be sent with a notification sound or not
     */
    fun showNotification(rawTitle: String, rawBody: String, sound: Boolean) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(context, MainActivity::class.java)
            val intentFlag = PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, intentFlag)

            val notificationManager = NotificationManagerCompat.from(context)

            val title = replaceQualifiers(context, rawTitle)
            val body = replaceQualifiers(context, rawBody)

            val channelId = if (sound) ChannelId.DEFAULT else ChannelId.SILENT
            val builder = NotificationCompat.Builder(context, channelId).apply {
                setSmallIcon(R.drawable.ic_notification)
                color = ContextCompat.getColor(context, R.color.color_notification)
                setContentTitle(title)
                setContentText(body)
                priority = NotificationCompat.PRIORITY_DEFAULT
                setContentIntent(pendingIntent)
                setStyle(NotificationCompat.BigTextStyle().bigText(body))
                setAutoCancel(true)
                if (sound) {
                    setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                    setVibrate(VibrationPattern.DEFAULT)
                }
            }
            notificationManager.notify(notificationId, builder.build())
        }
    }

    private fun replaceQualifiers(context: Context, s: String): String {
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val username = preferences.getString(context.getString(R.string.pref_key_nickname), null)
        if (s.contains(Qualifier.USERNAME) && username != null) {
            return s.replace(Qualifier.USERNAME, username)
        }
        return s
    }

    private val notificationId: Int
        get() = System.currentTimeMillis().toInt()
}
