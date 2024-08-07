package pl.piotrskiba.angularowo.main.offers.nav

import com.google.android.gms.ads.rewarded.RewardItem
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.PrizeOffer

interface OffersNavigator {

    fun displayAdOfferConfirmationDialog(adOffer: AdOffer, onConfirm: (adOffer: AdOffer) -> Unit)
    fun displayPrizeOfferConfirmationDialog(prizeOffer: PrizeOffer, onConfirm: (prizeOffer: PrizeOffer) -> Unit)
    fun displayRewardedAd(adId: String, onAdLoaded: () -> Unit, onAdWatched: (RewardItem) -> Unit, onAdLoadingFailure: () -> Unit)
    fun displayRewardedAdLoadingFailureDialog()
    fun displayAdOfferRedeemedDialog()
    fun displayPrizeOfferRedeemedDialog()
}
