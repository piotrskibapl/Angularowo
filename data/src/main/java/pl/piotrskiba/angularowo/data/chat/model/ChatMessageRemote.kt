package pl.piotrskiba.angularowo.data.chat.model

import pl.piotrskiba.angularowo.domain.chat.model.ChatMessageModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

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
        rank = RankModel.unknownRank(rank),
        message = message,
    )

fun List<ChatMessageRemote>.toDomain() =
    map { it.toDomain() }
