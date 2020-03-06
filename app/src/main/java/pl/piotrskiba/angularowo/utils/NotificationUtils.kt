package pl.piotrskiba.angularowo.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.activities.MainActivity

class NotificationUtils(private val context: Context) {

    fun createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
            createSilentNotificationChannel()
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.notification_channel_name)
            val description = context.getString(R.string.notification_channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(Constants.DEFAULT_CHANNEL_ID, name, importance)
            channel.description = description
            channel.vibrationPattern = Constants.DEFAULT_VIBRATE_PATTERN
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

    private fun createSilentNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = context.getString(R.string.notification_channel_name_silent)
            val description = context.getString(R.string.notification_channel_description_silent)
            val importance = NotificationManager.IMPORTANCE_DEFAULT

            val channel = NotificationChannel(Constants.SILENT_CHANNEL_ID, name, importance)
            channel.description = description
            channel.vibrationPattern = longArrayOf(0)
            channel.enableVibration(true)
            channel.setSound(null, null)

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(channel)
        }
    }

    fun showNotification(raw_title: String, raw_body: String, sound: Boolean) {
        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val notificationManager = NotificationManagerCompat.from(context)

        var title = raw_title
        var body = raw_body
        if (raw_title.contains(Constants.NOTIFICATION_USERNAME_QUALIFIER) || raw_body.contains(Constants.NOTIFICATION_USERNAME_QUALIFIER)) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

            val username = sharedPreferences.getString(context.getString(R.string.pref_key_nickname), null)
            username?.run {
                title = title.replace(Constants.NOTIFICATION_USERNAME_QUALIFIER, username)
                body = body.replace(Constants.NOTIFICATION_USERNAME_QUALIFIER, username)
            }
        }

        if (sound) {
            val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val builder = NotificationCompat.Builder(context, Constants.DEFAULT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(context.resources.getColor(R.color.color_notification))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setAutoCancel(true)
                    .setSound(soundUri)
                    .setVibrate(Constants.DEFAULT_VIBRATE_PATTERN)

            notificationManager.notify(notificationId, builder.build())
        } else {
            val builder = NotificationCompat.Builder(context, Constants.SILENT_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setColor(context.resources.getColor(R.color.color_notification))
                    .setContentTitle(title)
                    .setContentText(body)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setContentIntent(pendingIntent)
                    .setStyle(NotificationCompat.BigTextStyle()
                            .bigText(body))
                    .setAutoCancel(true)

            notificationManager.notify(notificationId, builder.build())
        }
    }

    private val notificationId: Int
        get() = System.currentTimeMillis().toInt()
}