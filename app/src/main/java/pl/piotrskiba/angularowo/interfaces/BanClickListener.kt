package pl.piotrskiba.angularowo.interfaces

import android.view.View
import pl.piotrskiba.angularowo.models.Ban

interface BanClickListener {
    fun onBanClick(view: View, clickedBan: Ban)
}