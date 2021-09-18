package pl.piotrskiba.angularowo.main.player.details.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.friend.usecase.MarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.ObserveIfPlayerIsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.UnmarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayerData
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import pl.piotrskiba.angularowo.main.player.model.toPlayerBannerData
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor(
    private val getPlayerDetailsFromUuidUseCase: GetPlayerDetailsFromUuidUseCase,
    private val observeIfPlayerIsFavoriteUseCase: ObserveIfPlayerIsFavoriteUseCase,
    private val markPlayerAsFavoriteUseCase: MarkPlayerAsFavoriteUseCase,
    private val unmarkPlayerAsFavoriteUseCase: UnmarkPlayerAsFavoriteUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    lateinit var player: DetailedPlayer
    val previewedPlayerBanner: MutableLiveData<PlayerBannerData> = MutableLiveData()
    val previewedPlayerDetails: MutableLiveData<DetailedPlayerData> = MutableLiveData()
    val isPreviewedPlayerFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val state = MutableLiveData<ViewModelState>(ViewModelState.Loading)
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        onRefresh()
        observeIfPlayerIsFavorite()
    }

    fun onRefresh() {
        loadPlayerDetails()
    }

    fun onFavoriteClick() {
        markPlayerAsFavoriteUseCase
            .execute(previewedPlayerBanner.value!!.uuid)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                {
                    // TODO: show snackbar
                },
                {
                    // TODO: provide error handling
                }
            )
    }

    fun onUnfavoriteClick() {
        unmarkPlayerAsFavoriteUseCase
            .execute(previewedPlayerBanner.value!!.uuid)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                {
                    // TODO: show snackbar
                },
                {
                    // TODO: provide error handling
                }
            )
    }

    private fun loadPlayerDetails() {
        state.value = ViewModelState.Loading
        disposables.add(getPlayerDetailsFromUuidUseCase
            .execute(
                BuildConfig.API_KEY,
                preferencesRepository.accessToken!!,
                previewedPlayerBanner.value!!.uuid
            )
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { detailedPlayer ->
                    state.value = ViewModelState.Loaded
                    previewedPlayerBanner.value = detailedPlayer.toPlayerBannerData()
                    previewedPlayerDetails.value = detailedPlayer.toUi()
                },
                { error ->
                    // TODO: provide error handling
                }
            )
        )
    }

    private fun observeIfPlayerIsFavorite() {
        observeIfPlayerIsFavoriteUseCase
            .execute(previewedPlayerBanner.value!!.uuid)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe { isPreviewedPlayerFavorite.value = it }
    }
}
