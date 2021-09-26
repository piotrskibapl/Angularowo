package pl.piotrskiba.angularowo.data.server.model

import pl.piotrskiba.angularowo.domain.server.model.Motd

class MotdRemote(
    val text: String,
    val url: String?,
    val textColor: String,
    val backgroundColor: String
)

fun MotdRemote.toDomain() = Motd(
    text,
    url,
    textColor,
    backgroundColor
)
