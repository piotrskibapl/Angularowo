package pl.piotrskiba.angularowo.main.offers.nav

import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer

interface OffersNavigator {

    fun onAdOfferClick(adOffer: AdOffer)
    fun onOfferClick(offer: Offer)
}
