package pl.piotrskiba.angularowo.main.offers.nav

import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer

interface OffersNavigator {

    fun displayAdOfferConfirmationDialog(adOffer: AdOffer, onConfirm: (adOffer: AdOffer) -> Unit)
    fun displayOfferConfirmationDialog(offer: Offer, onConfirm: (offer: Offer) -> Unit)
}
