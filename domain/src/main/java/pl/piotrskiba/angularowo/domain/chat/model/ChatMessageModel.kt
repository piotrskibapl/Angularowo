package pl.piotrskiba.angularowo.domain.chat.model

import pl.piotrskiba.angularowo.domain.rank.model.RankModel

data class ChatMessageModel(
    val uuid: String,
    val username: String,
    val rank: RankModel,
    val message: String,
)
