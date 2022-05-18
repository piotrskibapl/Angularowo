package pl.piotrskiba.angularowo.main.offers.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.offers.usecase.GetOffersInfoUseCase
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer
import pl.piotrskiba.angularowo.main.offers.model.OffersInfo
import pl.piotrskiba.angularowo.main.offers.model.toUi
import pl.piotrskiba.angularowo.main.offers.nav.OffersNavigator
import javax.inject.Inject

class OffersViewModel @Inject constructor(
    private val getOffersInfoUseCase: GetOffersInfoUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val offersInfo = MutableLiveData<OffersInfo>()
    val adOffersBinding = ItemBinding.of<AdOffer>(BR.adOffer, R.layout.ad_offer_list_item)
    val offersBinding = ItemBinding.of<Offer>(BR.offer, R.layout.offer_list_item)
    val state = MutableLiveData<ViewModelState>(Loading)
    val isDataLoaded = offersInfo.map { it != null }
    lateinit var navigator: OffersNavigator

    override fun onFirstCreate() {
        adOffersBinding.bindExtra(BR.navigator, navigator)
        offersBinding.bindExtra(BR.navigator, navigator)
        super.onFirstCreate()
        loadOffersInfo()
    }

    fun onRefresh() {
        loadOffersInfo()
    }

    private fun loadOffersInfo() {
        state.value = Loading
        disposables.add(
            getOffersInfoUseCase
                .execute(preferencesRepository.accessToken!!)
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
