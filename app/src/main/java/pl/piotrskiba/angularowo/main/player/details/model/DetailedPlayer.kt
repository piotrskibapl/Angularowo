package pl.piotrskiba.angularowo.main.player.details.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.utils.TextUtils
import java.text.NumberFormat

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

    fun playtimeText(context: Context) = TextUtils.formatPlaytime(context, playtime)
}

fun DetailedPlayerModel.toUi() = DetailedPlayer(
    username,
    skinUuid,
    rank.name,
    tokens,
    balance,
    playtime,
)
