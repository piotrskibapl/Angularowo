package pl.piotrskiba.angularowo.main.chat.model

import android.content.Context
import android.graphics.Color
import android.text.Spanned
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.main.base.MinecraftColor
import pl.piotrskiba.angularowo.utils.TextUtils

data class ChatMessage(
    val uuid: String,
    val username: String,
    val rank: RankModel,
    val message: String,
) {

    fun formattedMessage(context: Context): Spanned {
        val rankColor = String.format(
            "#%06X",
            0xFFFFFF and darkenColor(
                ContextCompat.getColor(context, MinecraftColor.colorCode[rank.colorCode] ?: MinecraftColor.default),
            ),
        )
        val messageColor = String.format(
            "#%06X",
            0xFFFFFF and darkenColor(
                ContextCompat.getColor(context, MinecraftColor.colorCode[rank.chatColorCode] ?: MinecraftColor.default),
            ),
        )
        val htmlText = TextUtils.replaceColorCodes(
            context,
            context.getString(
                R.string.chat_user_message,
                rank.name,
                username,
                message,
                rankColor,
                messageColor,
            ),
        )
        return HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
    }

    private fun darkenColor(color: Int): Int {
        val hsv = FloatArray(3)
        Color.colorToHSV(color, hsv)
        hsv[2] *= 1.4f
        return Color.HSVToColor(hsv)
    }
}

fun ChatMessageModel.toUi() =
    ChatMessage(
        uuid = uuid,
        username = username,
        rank = rank,
        message = message,
    )

fun List<ChatMessageModel>.toUi() =
    map { it.toUi() }
