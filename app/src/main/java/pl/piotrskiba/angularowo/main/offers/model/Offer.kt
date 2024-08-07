package pl.piotrskiba.angularowo.main.offers.model

import java.util.Calendar
import java.util.Date
import java.util.concurrent.TimeUnit

abstract class Offer(
    open val id: String,
    availabilityDate: Date?,
    redeemable: Boolean,
) {

    private val currentDate = Calendar.getInstance().time
    private val availabilityDateReached = (availabilityDate ?: currentDate).time <= currentDate.time
    val canRedeem = availabilityDateReached && redeemable
    val timeLeftVisible = !availabilityDateReached
    val timeLeftText = formatTimeDifference(
        availabilityDate ?: currentDate,
        currentDate,
    )

    private fun formatTimeDifference(from: Date, to: Date): String {
        val millis = from.time - to.time
        val days = (millis / TimeUnit.DAYS.toMillis(1)).toInt()
        val hours = (millis / TimeUnit.HOURS.toMillis(1) % 24).toInt()
        val minutes = (millis / TimeUnit.MINUTES.toMillis(1) % 60).toInt()
        val seconds = (millis / TimeUnit.SECONDS.toMillis(1) % 60).toInt()

        val builder = StringBuilder()
        if (days > 0) builder.append(" $days d")
        if (hours > 0) builder.append(" $hours h")
        if (minutes > 0 && days == 0) builder.append(" $minutes m")
        if (seconds > 0 && days == 0 && hours == 0) builder.append(" $seconds s")
        return builder.toString().trim()
    }
}
