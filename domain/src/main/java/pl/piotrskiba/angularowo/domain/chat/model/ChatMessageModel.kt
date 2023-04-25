package pl.piotrskiba.angularowo.domain.chat.model

data class ChatMessageModel(
    val uuid: String,
    val username: String,
    val rankName: String,
    val message: String,
)
