package pl.piotrskiba.angularowo.interfaces

import android.view.View
import pl.piotrskiba.angularowo.models.Player

interface PlayerClickListener {
    fun onPlayerClick(view: View, clickedPlayer: Player)
}