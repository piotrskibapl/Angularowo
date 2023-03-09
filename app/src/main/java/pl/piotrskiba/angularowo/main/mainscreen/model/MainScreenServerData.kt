package pl.piotrskiba.angularowo.main.mainscreen.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import kotlin.math.round

data class MainScreenServerData(
    private val playerCount: Int,
    private val tps: Double,
    private val displayTps: Boolean,
) {

    fun playerCountText(context: Context) =
        if (displayTps) {
            context.resources.getQuantityString(
                R.plurals.playercount_tps,
                playerCount,
                playerCount,
                tpsText(tps),
            )
        } else {
            context.resources.getQuantityString(
                R.plurals.playercount,
                playerCount,
                playerCount,
            )
        }

    private fun tpsText(tps: Double) =
        if (tps == round(tps)) {
            String.format("%d", tps.toInt())
        } else {
            String.format("%.1f", tps)
        }
}

fun ServerStatusModel.toUi(displayTps: Boolean) = MainScreenServerData(
    playerCount = playerCount,
    tps = tps,
    displayTps = displayTps,
)
