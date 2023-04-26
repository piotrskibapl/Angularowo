package pl.piotrskiba.angularowo.main.chat.model

import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.rank.model.Rank

data class ChatMessage(
    val uuid: String,
    val username: String,
    val rank: Rank,
    val message: String,
)

fun ChatMessageModel.toUi() =
    ChatMessage(
        uuid = uuid,
        username = username,
        rank = rank,
        message = message,
    )

fun List<ChatMessageModel>.toUi() =
    map { it.toUi() }
