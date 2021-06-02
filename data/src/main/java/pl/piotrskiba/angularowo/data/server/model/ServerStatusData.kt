package pl.piotrskiba.angularowo.data.server.model

import com.google.gson.annotations.SerializedName
import pl.piotrskiba.angularowo.domain.server.model.ServerStatus

class ServerStatusData(
    @field:SerializedName("player_count") val playerCount: Int,
    @field:SerializedName("tps_5") val tps: Double,
    val motd: MotdData?
)

fun ServerStatusData.toDomain() = ServerStatus(
    playerCount,
    tps,
    motd?.toDomain()
)
