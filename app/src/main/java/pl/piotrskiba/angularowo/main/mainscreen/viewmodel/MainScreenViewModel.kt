package pl.piotrskiba.angularowo.main.mainscreen.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import com.github.magneticflux.livedata.map
import com.github.magneticflux.livedata.zipTo
import io.reactivex.rxjava3.disposables.CompositeDisposable
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
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.domain.punishment.usecase.GetActivePlayerPunishmentsUseCase
import pl.piotrskiba.angularowo.domain.server.usecase.GetServerStatusUseCase
import pl.piotrskiba.angularowo.main.ban.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.ban.model.PunishmentBannerData
import pl.piotrskiba.angularowo.main.ban.model.toUi
import pl.piotrskiba.angularowo.main.mainscreen.model.MainScreenServerData
import pl.piotrskiba.angularowo.main.mainscreen.model.toUi
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayerData
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(
    private val getServerStatusUseCase: GetServerStatusUseCase,
    private val getPlayerDetailsFromUuidUseCase: GetPlayerDetailsFromUuidUseCase,
    private val getActivePlayerPunishmentsUseCase: GetActivePlayerPunishmentsUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    private val playerDataState = MutableLiveData<ViewModelState>(Loading)
    private val serverDataState = MutableLiveData<ViewModelState>(Loading)
    private val punishmentsDataState = MutableLiveData<ViewModelState>(Loading)
    val state = playerDataState
        .zipTo(serverDataState)
        .zipTo(punishmentsDataState)
        .map { state ->
            val errorState = state.first.first as? Error
                ?: state.first.second as? Error
                ?: state.second as? Error
            val loadingState = state.first.first as? Loading
                ?: state.first.second as? Loading
                ?: state.second as? Loading

            when (errorState != null) {
                true -> errorState
                false -> when (loadingState != null) {
                    true -> Loading
                    false -> Loaded
                }
            }
        }
    // exposed for MainViewModel synchronization
    val player = MutableLiveData<DetailedPlayerModel>()
    private val lastPlayerData = DetailedPlayerData(
        preferencesRepository.username!!,
        preferencesRepository.tokens,
        preferencesRepository.balance,
        preferencesRepository.skinUuid ?: preferencesRepository.uuid!!,
        preferencesRepository.playtime
    )
    val playerData = MutableLiveData(lastPlayerData)
    val serverData = MutableLiveData<MainScreenServerData>()
    val punishments: ObservableList<PunishmentBannerData> = ObservableArrayList()
    val punishmentsBinding = ItemBinding.of<PunishmentBannerData>(BR.punishment, R.layout.punishment_list_item)
    val isPunishmentListNotEmpty = MutableLiveData(false)
    lateinit var navigator: PunishmentListNavigator
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        punishmentsBinding.bindExtra(BR.navigator, navigator)
        onRefresh()
        // TODO: subscribe to Firebase app version topic
        // TODO: subscribe to Firebase player uuid topic
        // TODO: subscribe to Firebase new events topic
        // TODO: subscribe to Firebase private messages topic
        // TODO: subscribe to Firebase account incidents topic
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun onRefresh() {
        loadServerStatus()
        loadPlayer()
        loadActivePunishments()
    }

    private fun loadServerStatus() {
        serverDataState.value = Loading
        disposables.add(getServerStatusUseCase
            .execute(preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { serverStatus ->
                    serverDataState.value = Loaded
                    serverData.value = serverStatus.toUi()
                },
                { error ->
                    serverDataState.value = Error(error)
                }
            )
        )
    }

    private fun loadPlayer() {
        playerDataState.value = Loading
        disposables.add(getPlayerDetailsFromUuidUseCase
            .execute(
                preferencesRepository.accessToken!!,
                preferencesRepository.uuid!!
            )
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { detailedPlayer ->
                    playerDataState.value = Loaded
                    playerData.value = detailedPlayer.toUi()
                    player.value = detailedPlayer
                    savePlayer(detailedPlayer)
                    // TODO: subscribe to Firebase rank topic
                    // TODO: subscribe/unsubscribe to Firebase new reports topic
                },
                { error ->
                    playerDataState.value = Error(error)
                }
            )
        )
    }

    private fun savePlayer(player: DetailedPlayerModel) {
        preferencesRepository.skinUuid = player.skinUuid
        preferencesRepository.username = player.username
        preferencesRepository.balance = player.balance
        preferencesRepository.tokens = player.tokens
        preferencesRepository.playtime = player.playtime
    }

    private fun loadActivePunishments() {
        punishmentsDataState.value = Loading
        disposables.add(getActivePlayerPunishmentsUseCase
            .execute(
                preferencesRepository.accessToken!!,
                preferencesRepository.username!!
            )
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { punishmentModels ->
                    punishmentsDataState.value = Loaded
                    punishments.clear()
                    punishments.addAll(punishmentModels.toUi())
                    isPunishmentListNotEmpty.value = punishments.isNotEmpty()
                },
                { error ->
                    punishmentsDataState.value = Error(error)
                }
            )
        )
    }
}
