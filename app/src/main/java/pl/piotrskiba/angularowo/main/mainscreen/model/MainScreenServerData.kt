package pl.piotrskiba.angularowo.main.mainscreen.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.server.model.ServerStatus

data class MainScreenServerData(
    private val playerCount: Int
) {

    // TODO: map TPS
    fun playerCountText(context: Context) =
        context.resources.getQuantityString(R.plurals.playercount, playerCount, playerCount)
}

fun ServerStatus.toUi() = MainScreenServerData(
    playerCount
)
