package pl.piotrskiba.angularowo.main.mainscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import com.github.magneticflux.livedata.map
import com.github.magneticflux.livedata.zipTo
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.domain.punishment.usecase.GetActivePlayerPunishmentsUseCase
import pl.piotrskiba.angularowo.domain.server.usecase.GetServerStatusUseCase
import pl.piotrskiba.angularowo.main.mainscreen.model.MainScreenPlayerData
import pl.piotrskiba.angularowo.main.mainscreen.model.MainScreenServerData
import pl.piotrskiba.angularowo.main.mainscreen.model.toUi
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
    val player = MutableLiveData<DetailedPlayer>()
    private val lastPlayerData = MainScreenPlayerData(
        preferencesRepository.username!!,
        preferencesRepository.tokens,
        preferencesRepository.balance,
        preferencesRepository.skinUuid ?: preferencesRepository.uuid!!,
        preferencesRepository.playtime
    )
    val playerData = MutableLiveData(lastPlayerData)
    val serverData = MutableLiveData<MainScreenServerData>()
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        onRefresh()
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
            .execute(
                BuildConfig.API_KEY,
                preferencesRepository.accessToken!!
            )
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
                BuildConfig.API_KEY,
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
                },
                { error ->
                    playerDataState.value = Error(error)
                }
            )
        )
    }

    private fun savePlayer(player: DetailedPlayer) {
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
                BuildConfig.API_KEY,
                preferencesRepository.accessToken!!,
                preferencesRepository.username!!
            )
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { punishments ->
                    punishmentsDataState.value = Loaded
                    // TODO: process punishment list
                },
                { error ->
                    punishmentsDataState.value = Error(error)
                }
            )
        )
    }
}
