package pl.piotrskiba.angularowo.domain.server.model

data class Motd(
    val text: String,
    val url: String?,
    val textColor: String,
    val backgroundColor: String
)
