package pl.piotrskiba.angularowo.main.punishment.list.viewmodel

import androidx.lifecycle.MutableLiveData
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
import pl.piotrskiba.angularowo.domain.punishment.usecase.GetActivePunishmentsUseCase
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.details.toUi
import pl.piotrskiba.angularowo.main.punishment.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import pl.piotrskiba.angularowo.main.punishment.model.toPunishmentBannerData
import javax.inject.Inject

class PunishmentListViewModel @Inject constructor(
    private val getActivePunishmentsUseCase: GetActivePunishmentsUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading)
    val punishments: MutableList<DetailedPunishmentData> = mutableListOf()
    val punishmentBanners: MutableLiveData<List<PunishmentBannerData>> = MutableLiveData()
    val punishmentsBinding = ItemBinding.of<PunishmentBannerData>(BR.punishment, R.layout.punishment_list_item)
    lateinit var navigator: PunishmentListNavigator

    override fun onFirstCreate() {
        punishmentsBinding.bindExtra(BR.navigator, navigator)
        loadPunishmentList()
    }

    fun onRefresh() {
        loadPunishmentList()
    }

    private fun loadPunishmentList() {
        state.value = Loading
        disposables.add(getActivePunishmentsUseCase
            .execute(preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { punishmentModels ->
                    state.value = Loaded
                    punishments.clear()
                    punishments.addAll(punishmentModels.toUi())
                    punishmentBanners.value = punishmentModels.toPunishmentBannerData()
                },
                { error ->
                    state.value = Error(error)
                    // TODO: provide error handling
                }
            )
        )
    }
}
