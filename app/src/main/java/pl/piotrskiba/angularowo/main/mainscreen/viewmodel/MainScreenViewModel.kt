package pl.piotrskiba.angularowo.main.mainscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snakydesign.livedataextensions.map
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.cloudmessaging.usecase.UpdateCloudMessagingSubscriptionsUseCase
import pl.piotrskiba.angularowo.domain.mainscreen.usecase.GetMainScreenDataAndSavePlayerUseCase
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.mainscreen.model.MainScreenServerData
import pl.piotrskiba.angularowo.main.mainscreen.model.toUi
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayerData
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.details.toUi
import pl.piotrskiba.angularowo.main.punishment.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import pl.piotrskiba.angularowo.main.punishment.model.toPunishmentBannerData
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val getMainScreenDataAndSavePlayerUseCase: GetMainScreenDataAndSavePlayerUseCase,
    private val updateCloudMessagingSubscriptionsUseCase: UpdateCloudMessagingSubscriptionsUseCase,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    // exposed for MainViewModel synchronization
    val player = MutableLiveData<DetailedPlayerModel>()
    val playerData = MutableLiveData<DetailedPlayerData>()
    val serverData = MutableLiveData<MainScreenServerData>()
    val punishments: MutableList<DetailedPunishmentData> = mutableListOf()
    val punishmentBanners: MutableLiveData<List<PunishmentBannerData>> = MutableLiveData()
    val punishmentsBinding = ItemBinding.of<PunishmentBannerData>(BR.punishment, R.layout.punishment_list_item)
    val isPunishmentListNotEmpty = punishmentBanners.map { it.isNotEmpty() }
    lateinit var navigator: PunishmentListNavigator

    override fun onFirstCreate() {
        punishmentsBinding.bindExtra(BR.navigator, navigator)
        loadData()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadData()
    }

    private fun loadData() {
        disposables.add(
            getMainScreenDataAndSavePlayerUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    { mainScreenDataModel ->
                        state.value = Loaded
                        // TODO: use one model
                        serverData.value = mainScreenDataModel.serverStatusModel.toUi(mainScreenDataModel.detailedPlayerModel.rank.staff)
                        playerData.value = mainScreenDataModel.detailedPlayerModel.toUi()
                        punishments.clear()
                        punishments.addAll(mainScreenDataModel.playerPunishments.toUi())
                        punishmentBanners.value = mainScreenDataModel.playerPunishments.toPunishmentBannerData()
                        player.value = mainScreenDataModel.detailedPlayerModel
                        updateCloudMessagingSubscriptions()
                    },
                    { error ->
                        state.value = Error(error)
                    }
                )
        )
    }

    private fun updateCloudMessagingSubscriptions() {
        disposables.add(
            updateCloudMessagingSubscriptionsUseCase.execute(BuildConfig.VERSION_CODE)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe()
        )
    }
}
