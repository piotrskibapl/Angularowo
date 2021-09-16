package pl.piotrskiba.angularowo.main.player.details.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayerData
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor(
    private val getPlayerDetailsFromUuidUseCase: GetPlayerDetailsFromUuidUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    lateinit var player: DetailedPlayer
    lateinit var previewedPlayer: PlayerBannerData
    val previewedPlayerDetails: MutableLiveData<DetailedPlayerData> = MutableLiveData()
    val state = MutableLiveData<ViewModelState>(ViewModelState.Loading)
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        onRefresh()
    }

    fun onRefresh() {
        loadPlayerDetails()
    }

    private fun loadPlayerDetails() {
        state.value = ViewModelState.Loading
        disposables.add(getPlayerDetailsFromUuidUseCase
            .execute(BuildConfig.API_KEY, preferencesRepository.accessToken!!, player.uuid)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { detailedPlayer ->
                    state.value = ViewModelState.Loaded
                    previewedPlayerDetails.value = detailedPlayer.toUi()
                },
                { error ->
                    // TODO: provide error handling
                }
            )
        )
    }
}
