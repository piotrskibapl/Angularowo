package pl.piotrskiba.angularowo.main.mainscreen.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
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

    private val lastPlayerData = MainScreenPlayerData(
        preferencesRepository.username!!,
        preferencesRepository.tokens,
        preferencesRepository.balance,
        preferencesRepository.skinUuid!!,
        preferencesRepository.playtime
    )
    val playerData = MutableLiveData(lastPlayerData)
    val serverData = MutableLiveData<MainScreenServerData>()
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        loadServerStatus()
        loadPlayer()
        loadActivePunishments()
    }

    override fun onCleared() {
        disposables.clear()
    }

    private fun loadServerStatus() {
        disposables.add(getServerStatusUseCase
            .execute(
                BuildConfig.API_KEY,
                preferencesRepository.accessToken!!
            )
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { serverStatus ->
                    serverData.value = serverStatus.toUi()
                },
                { error ->
                    // TODO: provide error handling
                }
            )
        )
    }

    private fun loadPlayer() {
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
                    playerData.value = detailedPlayer.toUi()
                    // TODO: save data in preferences
                },
                { error ->
                    // TODO: provide error handling
                }
            )
        )
    }

    private fun loadActivePunishments() {
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
                    // TODO: process punishment list
                },
                { error ->
                    // TODO: provide error handling
                }
            )
        )
    }
}
