package pl.piotrskiba.angularowo.domain.server.model

data class ServerStatus(
    val playerCount: Int,
    val tps: Double,
    val motd: Motd?
)
