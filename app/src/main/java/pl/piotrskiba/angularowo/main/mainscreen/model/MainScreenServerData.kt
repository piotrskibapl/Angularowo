package pl.piotrskiba.angularowo.main.mainscreen.model

import android.content.Context
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel

data class MainScreenServerData(
    private val playerCount: Int
) {

    // TODO: map TPS
    fun playerCountText(context: Context) =
        context.resources.getQuantityString(R.plurals.playercount, playerCount, playerCount)
}

fun ServerStatusModel.toUi() = MainScreenServerData(
    playerCount
)
