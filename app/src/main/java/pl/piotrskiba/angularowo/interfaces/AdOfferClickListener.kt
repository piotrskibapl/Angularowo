package pl.piotrskiba.angularowo.interfaces

import android.view.View
import pl.piotrskiba.angularowo.models.AdOffer

interface AdOfferClickListener {
    fun onAdOfferClick(view: View, clickedAdOffer: AdOffer)
}