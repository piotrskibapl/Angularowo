package pl.piotrskiba.angularowo.main.mainscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.cloudmessaging.usecase.UpdateCloudMessagingSubscriptionsUseCase
import pl.piotrskiba.angularowo.domain.mainscreen.usecase.GetMainScreenDataAndSavePlayerUseCase
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.mainscreen.model.MainScreenData
import pl.piotrskiba.angularowo.main.mainscreen.model.toUi
import pl.piotrskiba.angularowo.main.mainscreen.nav.MainScreenNavigator
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.details.toUi
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBanner
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val getMainScreenDataAndSavePlayerUseCase: GetMainScreenDataAndSavePlayerUseCase,
    private val updateCloudMessagingSubscriptionsUseCase: UpdateCloudMessagingSubscriptionsUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val player = MutableLiveData<DetailedPlayerModel>() // exposed for MainViewModel synchronization
    val uiData = MutableLiveData<MainScreenData>()
    val punishments: MutableList<DetailedPunishmentData> = mutableListOf()
    val punishmentsBinding = ItemBinding.of<PunishmentBanner>(BR.punishment, R.layout.punishment_list_item)
    val isPunishmentListNotEmpty = uiData.map { it.punishments.isNotEmpty() }
    lateinit var navigator: MainScreenNavigator

    override fun onFirstCreate() {
        loadData()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadData()
    }

    fun onMotdClick(url: String) {
        navigator.openBrowser(url)
    }

    private fun loadData() {
        disposables.add(
            getMainScreenDataAndSavePlayerUseCase.execute()
                .applyDefaultSchedulers(facade)
                .subscribe(
                    { mainScreenDataModel ->
                        state.value = Loaded
                        uiData.value = mainScreenDataModel.toUi()
                        punishments.clear()
                        punishments.addAll(mainScreenDataModel.playerPunishments.toUi())
                        player.value = mainScreenDataModel.detailedPlayerModel
                        updateCloudMessagingSubscriptions()
                    },
                    { error ->
                        state.value = Error(error)
                    },
                ),
        )
    }

    private fun updateCloudMessagingSubscriptions() {
        disposables.add(
            updateCloudMessagingSubscriptionsUseCase.execute(BuildConfig.VERSION_CODE)
                .applyDefaultSchedulers(facade)
                .subscribe(),
        )
    }
}
