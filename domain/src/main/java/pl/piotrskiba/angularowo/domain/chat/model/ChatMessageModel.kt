package pl.piotrskiba.angularowo.domain.chat.model

import pl.piotrskiba.angularowo.domain.rank.model.Rank

data class ChatMessageModel(
    val uuid: String,
    val username: String,
    val rank: Rank,
    val message: String,
)
