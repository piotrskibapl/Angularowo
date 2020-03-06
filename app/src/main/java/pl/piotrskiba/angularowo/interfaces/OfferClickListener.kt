package pl.piotrskiba.angularowo.interfaces

import android.view.View
import pl.piotrskiba.angularowo.models.Offer

interface OfferClickListener {
    fun onOfferClick(view: View, clickedOffer: Offer)
}