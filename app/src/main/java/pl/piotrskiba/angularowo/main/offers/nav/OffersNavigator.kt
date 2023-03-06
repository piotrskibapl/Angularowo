package pl.piotrskiba.angularowo.main.offers.nav

import com.google.android.gms.ads.rewarded.RewardItem
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer

interface OffersNavigator {

    fun displayAdOfferConfirmationDialog(adOffer: AdOffer, onConfirm: (adOffer: AdOffer) -> Unit)
    fun displayOfferConfirmationDialog(offer: Offer, onConfirm: (offer: Offer) -> Unit)
    fun displayRewardedAd(adId: String, onAdWatched: (RewardItem) -> Unit, onAdLoadingFailure: () -> Unit)
    fun displayRewardedAdLoadingFailureDialog()
    fun displayAdOfferRedeemedDialog()
}
