package pl.piotrskiba.angularowo.main.offers.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentOffersBinding
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer
import pl.piotrskiba.angularowo.main.offers.nav.OffersNavigator
import pl.piotrskiba.angularowo.main.offers.viewmodel.OffersViewModel

class OffersFragment : BaseFragment<OffersViewModel>(OffersViewModel::class), OffersNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setupBinding(layoutInflater, container).root
    }

    override fun displayAdOfferConfirmationDialog(
        adOffer: AdOffer,
        onConfirm: (adOffer: AdOffer) -> Unit,
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.offers_ad_question_dialog_title)
            .setMessage(
                resources.getQuantityString(
                    R.plurals.offers_ad_question_dialog_description,
                    adOffer.points,
                    adOffer.points,
                ),
            )
            .setPositiveButton(R.string.button_yes) { _: DialogInterface, _: Int ->
                onConfirm(adOffer)
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }

    override fun displayOfferConfirmationDialog(offer: Offer, onConfirm: (offer: Offer) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.offers_offer_question_dialog_title)
            .setMessage(
                resources.getQuantityString(
                    R.plurals.offers_offer_question_dialog_description,
                    offer.price,
                    offer.title,
                    offer.price,
                ),
            )
            .setPositiveButton(R.string.button_yes) { _: DialogInterface, _: Int ->
                onConfirm(offer)
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }

    override fun displayRewardedAd(adId: String, onAdLoaded: () -> Unit, onAdWatched: (RewardItem) -> Unit, onAdLoadingFailure: () -> Unit) {
        val extras = Bundle().apply {
            putString("npa", "1")
        }
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        RewardedAd.load(
            requireContext(),
            adId,
            adRequest,
            object : RewardedAdLoadCallback() {

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    onAdLoaded()
                    rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {

                        override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                            onAdLoadingFailure()
                        }
                    }
                    rewardedAd.show(requireActivity()) { onAdWatched(it) }
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    onAdLoadingFailure()
                }
            },
        )
    }

    override fun displayRewardedAdLoadingFailureDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.offers_no_ads_dialog_title)
            .setMessage(R.string.offers_no_ads_dialog_description)
            .setPositiveButton(R.string.button_dismiss) { _, _ -> }
            .show()
    }

    override fun displayAdOfferRedeemedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.offers_ad_offer_redeemed_dialog_title)
            .setMessage(R.string.offers_ad_offer_redeemed_dialog_description)
            .setPositiveButton(R.string.button_dismiss) { _, _ -> }
            .show()
    }

    override fun displayOfferRedeemedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.offers_offer_redeemed_dialog_title)
            .setMessage(R.string.offers_offer_redeemed_dialog_description)
            .setPositiveButton(R.string.button_dismiss) { _, _ -> }
            .show()
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentOffersBinding {
        val binding = FragmentOffersBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }
}
