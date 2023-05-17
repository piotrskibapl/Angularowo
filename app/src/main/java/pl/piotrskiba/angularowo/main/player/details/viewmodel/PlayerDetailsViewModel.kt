package pl.piotrskiba.angularowo.main.player.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.base.extensions.serializable
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.friend.usecase.MarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.ObserveIfPlayerIsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.UnmarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.usecase.CheckIfShouldDisplayFavoriteShowcaseUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayer
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.player.details.nav.PlayerDetailsNavigator
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner
import pl.piotrskiba.angularowo.main.player.model.toPlayerBannerData
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor(
    private val getPlayerDetailsFromUuidUseCase: GetPlayerDetailsFromUuidUseCase,
    private val observeIfPlayerIsFavoriteUseCase: ObserveIfPlayerIsFavoriteUseCase,
    private val markPlayerAsFavoriteUseCase: MarkPlayerAsFavoriteUseCase,
    private val unmarkPlayerAsFavoriteUseCase: UnmarkPlayerAsFavoriteUseCase,
    private val checkIfShouldDisplayFavoriteShowcaseUseCase: CheckIfShouldDisplayFavoriteShowcaseUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    lateinit var navigator: PlayerDetailsNavigator
    val player: DetailedPlayerModel by lazy { intent.serializable(Constants.EXTRA_PLAYER)!! }
    val previewedPlayerBanner: MutableLiveData<PlayerBanner> by lazy { MutableLiveData(intent.serializable(Constants.EXTRA_PREVIEWED_PLAYER)) }
    val previewedPlayerDetails: MutableLiveData<DetailedPlayer> = MutableLiveData()
    val playerBannerVisible: LiveData<Boolean> by lazy { previewedPlayerBanner.map { it != null } }
    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    private val previewedPlayerUuid: String by lazy { intent.getStringExtra(Constants.EXTRA_UUID)!! }
    private var isPreviewedPlayerFavorite: Boolean = false

    override fun onFirstCreate() {
        loadPlayerDetails()
        observeIfPlayerIsFavorite()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadPlayerDetails()
    }

    fun onFavoriteClick() {
        disposables.add(
            markPlayerAsFavoriteUseCase
                .execute(previewedPlayerUuid)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    {
                        navigator.displayMarkedAsFavoriteSnackbar()
                    },
                    {
                        navigator.displayGenericErrorSnackbar()
                    }
                )
        )
    }

    fun onUnfavoriteClick() {
        disposables.add(
            unmarkPlayerAsFavoriteUseCase
                .execute(previewedPlayerUuid)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    {
                        navigator.displayUnmarkedAsFavoriteSnackbar()
                    },
                    {
                        navigator.displayGenericErrorSnackbar()
                    }
                )
        )
    }

    private fun loadPlayerDetails() {
        disposables.add(getPlayerDetailsFromUuidUseCase
            .execute(previewedPlayerUuid)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { detailedPlayer ->
                    state.value = Loaded
                    previewedPlayerBanner.value = detailedPlayer.toPlayerBannerData(isPreviewedPlayerFavorite)
                    previewedPlayerDetails.value = detailedPlayer.toUi()
                    checkIfShouldDisplayFavoriteShowcase()
                },
                { error ->
                    state.value = Error(error)
                }
            )
        )
    }

    private fun checkIfShouldDisplayFavoriteShowcase() {
        disposables.add(
            checkIfShouldDisplayFavoriteShowcaseUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { shouldShow ->
                    if (shouldShow) {
                        navigator.displayFavoriteShowcase()
                    }
                }
        )
    }

    private fun observeIfPlayerIsFavorite() {
        disposables.add(
            observeIfPlayerIsFavoriteUseCase
                .execute(previewedPlayerUuid)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe {
                    isPreviewedPlayerFavorite = it
                    previewedPlayerBanner.value = previewedPlayerBanner.value?.copy(isFavorite = it)
                }
        )
    }
}
