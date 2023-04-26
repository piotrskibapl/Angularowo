package pl.piotrskiba.angularowo.main.chat.model

import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel

data class ChatMessage(
    val uuid: String,
    val username: String,
    val rankName: String,
    val message: String,
)

fun ChatMessageModel.toUi() =
    ChatMessage(
        uuid = uuid,
        username = username,
        rankName = rankName,
        message = message,
    )

fun List<ChatMessageModel>.toUi() =
    map { it.toUi() }
