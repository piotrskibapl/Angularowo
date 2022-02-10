package pl.piotrskiba.angularowo.main.offers.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
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
        savedInstanceState: Bundle?
    ): View {
        val binding = setupBinding(layoutInflater, container)
        setupActionBar()
        return binding.root
    }

    override fun displayAdOfferConfirmationDialog(
        adOffer: AdOffer,
        onConfirm: (adOffer: AdOffer) -> Unit
    ) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.ad_question)
            .setMessage(
                resources.getQuantityString(
                    R.plurals.ad_question_description,
                    adOffer.points,
                    adOffer.points
                )
            )
            .setPositiveButton(R.string.button_yes) { _: DialogInterface, _: Int ->
                onConfirm(adOffer)
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }

    override fun displayOfferConfirmationDialog(offer: Offer, onConfirm: (offer: Offer) -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.offer_question)
            .setMessage(
                resources.getQuantityString(
                    R.plurals.offer_question_description,
                    offer.price,
                    offer.title,
                    offer.price
                )
            )
            .setPositiveButton(R.string.button_yes) { _: DialogInterface, _: Int ->
                onConfirm(offer)
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }

    override fun displayRewardedAd(adId: String, onAdWatched: () -> Unit, onAdLoadingFailure: () -> Unit) {
        val extras = Bundle().apply {
            putString("npa", "1")
        }
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()
        RewardedAd.load(requireContext(), adId, adRequest, object : RewardedAdLoadCallback() {

            override fun onAdLoaded(rewardedAd: RewardedAd) {
                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) { onAdLoadingFailure() }
                }
                rewardedAd.show(requireActivity()) { onAdWatched() }
            }

            override fun onAdFailedToLoad(p0: LoadAdError) { onAdLoadingFailure() }
        })
    }

    override fun displayRewardedAdLoadingFailureDialog() {
        // TODO: show dialog
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOffersBinding {
        val binding = FragmentOffersBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun setupActionBar() {
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_free_ranks)
    }
}
