package pl.piotrskiba.angularowo.main.punishment.list.viewmodel

import androidx.lifecycle.MutableLiveData
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
import pl.piotrskiba.angularowo.domain.punishment.usecase.GetActivePunishmentsUseCase
import pl.piotrskiba.angularowo.main.punishment.details.model.DetailedPunishment
import pl.piotrskiba.angularowo.main.punishment.details.model.toUi
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBanner
import pl.piotrskiba.angularowo.main.punishment.model.toPunishmentBanners
import javax.inject.Inject

class PunishmentListViewModel @Inject constructor(
    private val getActivePunishmentsUseCase: GetActivePunishmentsUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val punishments: MutableList<DetailedPunishment> = mutableListOf()
    val punishmentBanners: MutableLiveData<List<PunishmentBanner>> = MutableLiveData()
    val punishmentsBinding = ItemBinding.of<PunishmentBanner>(BR.punishment, R.layout.punishment_list_item)

    override fun onFirstCreate() {
        loadPunishmentList()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadPunishmentList()
    }

    private fun loadPunishmentList() {
        disposables.add(
            getActivePunishmentsUseCase.execute()
                .applyDefaultSchedulers(facade)
                .subscribe(
                    { punishmentModels ->
                        state.value = Loaded
                        punishments.clear()
                        punishments.addAll(punishmentModels.toUi())
                        punishmentBanners.value = punishmentModels.toPunishmentBanners()
                    },
                    { error ->
                        state.value = Error(error)
                    },
                ),
        )
    }
}
