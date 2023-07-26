package pl.piotrskiba.angularowo.main.player.details.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.utils.TextUtils
import pl.piotrskiba.angularowo.utils.UrlUtils
import java.text.NumberFormat

data class DetailedPlayer(
    val username: String,
    val rankName: String,
    private val tokens: Int,
    private val balance: Float,
    private val skinUuid: String,
    private val playtime: Long,
) {

    fun greetingText(context: Context) = context.getString(R.string.main_screen_greeting, username)

    fun balanceText() = "$${NumberFormat.getInstance().format(balance.toInt())}"

    fun tokensText(): String = NumberFormat.getInstance().format(tokens)

    fun bodyImageUrl() = UrlUtils.buildBodyUrl(skinUuid, true)

    fun playtimeText(context: Context) = TextUtils.formatPlaytime(context, playtime)
}

fun DetailedPlayerModel.toUi() = DetailedPlayer(
    username,
    rank.name,
    tokens,
    balance,
    skinUuid,
    playtime,
)
