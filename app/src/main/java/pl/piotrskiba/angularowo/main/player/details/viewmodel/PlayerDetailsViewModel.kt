package pl.piotrskiba.angularowo.main.player.details.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.friend.usecase.MarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.UnmarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.CheckIfShouldDisplayFavoriteShowcaseUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.GetPlayerDetailsFromUuidUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.ObservePlayerDetailsMenuItemsVisibilityUseCase
import pl.piotrskiba.angularowo.domain.punishment.usecase.PunishPlayerUseCase
import pl.piotrskiba.angularowo.main.player.details.model.DetailedPlayer
import pl.piotrskiba.angularowo.main.player.details.model.PlayerDetailsMenuItemsVisibility
import pl.piotrskiba.angularowo.main.player.details.model.PunishmentType
import pl.piotrskiba.angularowo.main.player.details.model.toDomain
import pl.piotrskiba.angularowo.main.player.details.model.toUi
import pl.piotrskiba.angularowo.main.player.details.nav.PlayerDetailsNavigator
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsFragmentArgs
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner
import pl.piotrskiba.angularowo.main.player.model.toPlayerBannerData
import javax.inject.Inject

class PlayerDetailsViewModel @Inject constructor(
    private val getPlayerDetailsFromUuidUseCase: GetPlayerDetailsFromUuidUseCase,
    private val observePlayerDetailsMenuItemsVisibilityUseCase: ObservePlayerDetailsMenuItemsVisibilityUseCase,
    private val markPlayerAsFavoriteUseCase: MarkPlayerAsFavoriteUseCase,
    private val unmarkPlayerAsFavoriteUseCase: UnmarkPlayerAsFavoriteUseCase,
    private val checkIfShouldDisplayFavoriteShowcaseUseCase: CheckIfShouldDisplayFavoriteShowcaseUseCase,
    private val punishPlayerUseCase: PunishPlayerUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    lateinit var args: PlayerDetailsFragmentArgs
    lateinit var navigator: PlayerDetailsNavigator
    val previewedPlayerBanner: MutableLiveData<PlayerBanner> by lazy { MutableLiveData(args.previewedPlayerBanner) }
    val previewedPlayerDetails: MutableLiveData<DetailedPlayer> = MutableLiveData()
    val playerBannerVisible: LiveData<Boolean> by lazy { previewedPlayerBanner.map { it != null } }
    val menuItemsVisibility: MutableLiveData<PlayerDetailsMenuItemsVisibility> = MutableLiveData()
    val state = MutableLiveData<ViewModelState>(Loading.Fetch)

    override fun onFirstCreate() {
        loadPlayerDetails()
        observeMenuItemsVisibility()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadPlayerDetails()
    }

    fun onFavoriteClick() {
        disposables.add(
            markPlayerAsFavoriteUseCase.execute(args.previewedPlayerUuid, previewedPlayerDetails.value!!.username)
                .applyDefaultSchedulers(facade)
                .subscribe(
                    {
                        navigator.displayMarkedAsFavoriteSnackbar()
                    },
                    {
                        navigator.displayGenericErrorSnackbar()
                    },
                ),
        )
    }

    fun onUnfavoriteClick() {
        disposables.add(
            unmarkPlayerAsFavoriteUseCase.execute(args.previewedPlayerUuid, previewedPlayerDetails.value!!.username)
                .applyDefaultSchedulers(facade)
                .subscribe(
                    {
                        navigator.displayUnmarkedAsFavoriteSnackbar()
                    },
                    {
                        navigator.displayGenericErrorSnackbar()
                    },
                ),
        )
    }

    fun onPunish(type: PunishmentType, reason: String, time: Long?) {
        state.value = Loading.Send
        disposables.add(
            punishPlayerUseCase.execute(args.previewedPlayerUuid, reason, type.toDomain(), time)
                .applyDefaultSchedulers(facade)
                .subscribe(
                    {
                        state.value = Loaded
                        navigator.displayPunishmentSuccessDialog(type)
                    },
                    {
                        state.value = Loaded
                        navigator.displayPunishmentErrorDialog()
                    },
                ),
        )
    }

    private fun loadPlayerDetails() {
        disposables.add(
            getPlayerDetailsFromUuidUseCase.execute(args.previewedPlayerUuid)
                .applyDefaultSchedulers(facade)
                .subscribe(
                    { detailedPlayer ->
                        state.value = Loaded
                        previewedPlayerBanner.value = detailedPlayer.toPlayerBannerData(isFavorite = menuItemsVisibility.value?.unfavorite ?: false)
                        previewedPlayerDetails.value = detailedPlayer.toUi()
                        checkIfShouldDisplayFavoriteShowcase()
                    },
                    { error ->
                        state.value = Error(error)
                    },
                ),
        )
    }

    private fun checkIfShouldDisplayFavoriteShowcase() {
        disposables.add(
            checkIfShouldDisplayFavoriteShowcaseUseCase.execute(args.previewedPlayerUuid)
                .applyDefaultSchedulers(facade)
                .subscribe { shouldShow ->
                    if (shouldShow) {
                        navigator.displayFavoriteShowcase()
                    }
                },
        )
    }

    private fun observeMenuItemsVisibility() {
        disposables.add(
            observePlayerDetailsMenuItemsVisibilityUseCase.execute(args.previewedPlayerUuid)
                .applyDefaultSchedulers(facade)
                .subscribe { menuItemsVisibilityModel ->
                    menuItemsVisibility.value = menuItemsVisibilityModel.toUi()
                    previewedPlayerBanner.value = previewedPlayerBanner.value?.copy(isFavorite = menuItemsVisibilityModel.unfavorite)
                },
        )
    }
}
