package pl.piotrskiba.angularowo.main.offers.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.android.gms.ads.rewarded.RewardItem
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.offers.usecase.GetOffersInfoUseCase
import pl.piotrskiba.angularowo.domain.offers.usecase.RedeemAdOfferUseCase
import pl.piotrskiba.angularowo.domain.offers.usecase.RedeemOfferUseCase
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer
import pl.piotrskiba.angularowo.main.offers.model.OffersInfo
import pl.piotrskiba.angularowo.main.offers.model.toUi
import pl.piotrskiba.angularowo.main.offers.nav.OffersNavigator
import javax.inject.Inject

class OffersViewModel @Inject constructor(
    private val getOffersInfoUseCase: GetOffersInfoUseCase,
    private val redeemAdOfferUseCase: RedeemAdOfferUseCase,
    private val redeemOfferUseCase: RedeemOfferUseCase,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val offersInfo = MutableLiveData<OffersInfo>()
    val adOffersBinding = ItemBinding.of<AdOffer>(BR.adOffer, R.layout.ad_offer_list_item)
    val offersBinding = ItemBinding.of<Offer>(BR.offer, R.layout.offer_list_item)
    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val isDataLoaded = offersInfo.map { it != null }
    lateinit var navigator: OffersNavigator

    override fun onFirstCreate() {
        adOffersBinding.bindExtra(BR.viewModel, this)
        offersBinding.bindExtra(BR.viewModel, this)
        super.onFirstCreate()
        loadOffersInfo()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadOffersInfo()
    }

    fun onAdOfferClick(adOffer: AdOffer) {
        navigator.displayAdOfferConfirmationDialog(adOffer) {
            state.value = Loading.Send
            navigator.displayRewardedAd(
                adOffer.adId,
                ::onAdWatched,
                ::onAdLoadingFailure
            )
        }
    }

    fun onOfferClick(offer: Offer) {
        navigator.displayOfferConfirmationDialog(offer) {
            state.value = Loading.Send
            disposables.add(
                redeemOfferUseCase.execute(offer.id)
                    .subscribeOn(facade.io())
                    .observeOn(facade.ui())
                    .subscribe {
                        navigator.displayOfferRedeemedDialog()
                        loadOffersInfo()
                    }
            )
        }
    }

    private fun onAdWatched(rewardItem: RewardItem) {
        state.value = Loading.Fetch
        disposables.add(
            redeemAdOfferUseCase.execute(rewardItem.type)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe {
                    navigator.displayAdOfferRedeemedDialog()
                    loadOffersInfo()
                }
        )
    }

    private fun onAdLoadingFailure() {
        state.value = Loaded
        navigator.displayRewardedAdLoadingFailureDialog()
    }

    private fun loadOffersInfo() {
        disposables.add(
            getOffersInfoUseCase
                .execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    { offersInfoModel ->
                        offersInfo.value = offersInfoModel.toUi()
                        state.value = Loaded
                    },
                    { error ->
                        state.value = Error(error)
                    })
        )
    }
}
