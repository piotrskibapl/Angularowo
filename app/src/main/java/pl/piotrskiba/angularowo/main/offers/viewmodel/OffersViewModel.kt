package pl.piotrskiba.angularowo.main.offers.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.google.android.gms.ads.rewarded.RewardItem
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.offers.usecase.GetOffersInfoUseCase
import pl.piotrskiba.angularowo.domain.offers.usecase.RedeemAdOfferUseCase
import pl.piotrskiba.angularowo.domain.offers.usecase.RedeemPrizeOfferUseCase
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.OffersInfo
import pl.piotrskiba.angularowo.main.offers.model.PrizeOffer
import pl.piotrskiba.angularowo.main.offers.model.toUi
import pl.piotrskiba.angularowo.main.offers.nav.OffersNavigator
import javax.inject.Inject

class OffersViewModel @Inject constructor(
    private val getOffersInfoUseCase: GetOffersInfoUseCase,
    private val redeemAdOfferUseCase: RedeemAdOfferUseCase,
    private val redeemPrizeOfferUseCase: RedeemPrizeOfferUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val offersInfo = MutableLiveData<OffersInfo>()
    val adOffersBinding = ItemBinding.of<AdOffer>(BR.adOffer, R.layout.ad_offer_list_item)
    val prizeOffersBinding = ItemBinding.of<PrizeOffer>(BR.prizeOffer, R.layout.prize_offer_list_item)
    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val isDataLoaded = offersInfo.map { it != null }
    lateinit var navigator: OffersNavigator

    override fun onFirstCreate() {
        adOffersBinding.bindExtra(BR.viewModel, this)
        prizeOffersBinding.bindExtra(BR.viewModel, this)
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
                ::onAdLoaded,
                ::onAdWatched,
                ::onAdLoadingFailure,
            )
        }
    }

    fun onPrizeOfferClick(prizeOffer: PrizeOffer) {
        navigator.displayPrizeOfferConfirmationDialog(prizeOffer) {
            state.value = Loading.Send
            disposables.add(
                redeemPrizeOfferUseCase.execute(prizeOffer.id)
                    .applyDefaultSchedulers(facade)
                    .subscribe {
                        navigator.displayPrizeOfferRedeemedDialog()
                        loadOffersInfo()
                    },
            )
        }
    }

    private fun onAdLoaded() {
        state.value = Loaded
    }

    private fun onAdWatched(rewardItem: RewardItem) {
        state.value = Loading.Fetch
        disposables.add(
            redeemAdOfferUseCase.execute(rewardItem.type)
                .applyDefaultSchedulers(facade)
                .subscribe {
                    navigator.displayAdOfferRedeemedDialog()
                    loadOffersInfo()
                },
        )
    }

    private fun onAdLoadingFailure() {
        state.value = Loaded
        navigator.displayRewardedAdLoadingFailureDialog()
    }

    private fun loadOffersInfo() {
        disposables.add(
            getOffersInfoUseCase.execute()
                .applyDefaultSchedulers(facade)
                .subscribe(
                    { offersInfoModel ->
                        offersInfo.value = offersInfoModel.toUi()
                        state.value = Loaded
                    },
                    { error ->
                        state.value = Error(error)
                    },
                ),
        )
    }
}
