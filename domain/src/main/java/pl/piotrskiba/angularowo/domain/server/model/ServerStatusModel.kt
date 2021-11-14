package pl.piotrskiba.angularowo.domain.server.model

data class ServerStatusModel(
    val playerCount: Int,
    val tps: Double,
    val motd: MotdModel?
)
