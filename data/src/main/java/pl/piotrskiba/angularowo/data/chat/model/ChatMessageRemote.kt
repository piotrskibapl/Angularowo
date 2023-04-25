package pl.piotrskiba.angularowo.data.chat.model

import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel

data class ChatMessageRemote(
    val uuid: String,
    val username: String,
    val rank: String,
    val message: String,
)

fun ChatMessageRemote.toDomain() =
    ChatMessageModel(
        uuid = uuid,
        username = username,
        rankName = rank,
        message = message,
    )

fun List<ChatMessageRemote>.toDomain() =
    map { it.toDomain() }