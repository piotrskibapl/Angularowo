package pl.piotrskiba.angularowo.main.player.details.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

data class DetailedPlayer(
    val username: String,
    val skinUuid: String,
    val rankName: String,
    private val tokens: Int,
    private val balance: Float,
    private val playtime: Long,
) {

    fun greetingText(context: Context) = context.getString(R.string.main_screen_greeting, username)

    fun balanceText() = "$${NumberFormat.getInstance().format(balance.toInt())}"

    fun tokensText(): String = NumberFormat.getInstance().format(tokens)

    fun playtimeText(context: Context): String {
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
}

fun DetailedPlayerModel.toUi() = DetailedPlayer(
    username,
    skinUuid,
    rank.name,
    tokens,
    balance,
    playtime,
)
