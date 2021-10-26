package pl.piotrskiba.angularowo.data.server.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.server.model.ServerStatus

class ServerStatusRemote(
    @field:SerializedName("player_count") val playerCount: Int,
    @field:SerializedName("tps_5") val tps: Double,
    val motd: MotdRemote?
)

fun ServerStatusRemote.toDomain() = ServerStatus(
    playerCount,
    tps,
    motd?.toDomain()
)