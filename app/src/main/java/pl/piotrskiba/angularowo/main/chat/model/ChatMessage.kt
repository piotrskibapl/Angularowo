package pl.piotrskiba.angularowo.main.chat.model

import android.content.Context
import android.graphics.Color
import android.text.Spanned
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.utils.TextUtils

data class ChatMessage(
    val uuid: String,
    val username: String,
    val rank: RankModel,
    val message: String,
) {

    private val colorCodesMap = mapOf(
        "0" to R.color.color_minecraft_0,
        "1" to R.color.color_minecraft_1,
        "2" to R.color.color_minecraft_2,
        "3" to R.color.color_minecraft_3,
        "4" to R.color.color_minecraft_4,
        "5" to R.color.color_minecraft_5,
        "6" to R.color.color_minecraft_6,
        "7" to R.color.color_minecraft_7,
        "8" to R.color.color_minecraft_8,
        "9" to R.color.color_minecraft_9,
        "a" to R.color.color_minecraft_a,
        "b" to R.color.color_minecraft_b,
        "c" to R.color.color_minecraft_c,
        "d" to R.color.color_minecraft_d,
        "e" to R.color.color_minecraft_e,
        "f" to R.color.color_minecraft_f,
    )

    fun formattedMessage(context: Context): Spanned {
        val rankColor = String.format(
            "#%06X",
            0xFFFFFF and darkenColor(
                ContextCompat.getColor(context, colorCodesMap[rank.colorCode] ?: 7),
            ),
        )
        val messageColor = String.format(
            "#%06X",
            0xFFFFFF and darkenColor(
                ContextCompat.getColor(context, colorCodesMap[rank.chatColorCode] ?: 7),
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
