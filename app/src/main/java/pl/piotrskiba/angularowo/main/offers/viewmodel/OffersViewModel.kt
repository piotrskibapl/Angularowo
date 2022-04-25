package pl.piotrskiba.angularowo.main.offers.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.offers.usecase.GetOffersInfoUseCase
import pl.piotrskiba.angularowo.main.offers.model.OffersInfo
import pl.piotrskiba.angularowo.main.offers.model.toUi
import javax.inject.Inject

class OffersViewModel @Inject constructor(
    private val getOffersInfoUseCase: GetOffersInfoUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val offersInfo = MutableLiveData<OffersInfo>()
    val state = MutableLiveData<ViewModelState>(Loading)

    override fun onFirstCreate() {
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
