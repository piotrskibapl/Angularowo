package pl.piotrskiba.angularowo.main.chat.model

import android.content.Context
import android.text.Spanned
import androidx.core.text.HtmlCompat
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.utils.ColorUtils
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
            0xFFFFFF and ColorUtils.changeBrightness(
                ColorUtils.getColorFromCode(
                    context,
                    rank.colorCode,
                ),
                1.4f,
            ),
        )
        val messageColor = String.format(
            "#%06X",
            0xFFFFFF and ColorUtils.changeBrightness(
                ColorUtils.getColorFromCode(
                    context,
                    rank.chatColorCode,
                ),
                1.4f,
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
