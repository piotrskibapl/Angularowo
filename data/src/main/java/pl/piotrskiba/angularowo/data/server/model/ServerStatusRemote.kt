package pl.piotrskiba.angularowo.data.server.model

import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel

class ServerStatusRemote(
    val player_count: Int,
    val tps_5: Double,
    val motd: MotdRemote?,
)

fun ServerStatusRemote.toDomain() = ServerStatusModel(
    player_count,
    tps_5,
    motd?.toDomain(),
)
