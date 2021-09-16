package pl.piotrskiba.angularowo.main.player.details.viewmodel

import io.reactivex.rxjava3.disposables.CompositeDisposable
import pl.piotrskiba.angularowo.BuildConfig
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
    lateinit var previewedPlayerDetails: DetailedPlayerData
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        onRefresh()
    }

    fun onRefresh() {
        // TODO: add refresh listener
        loadPlayerDetails()
    }

    private fun loadPlayerDetails() {
        disposables.add(getPlayerDetailsFromUuidUseCase
            .execute(BuildConfig.API_KEY, preferencesRepository.accessToken!!, player.uuid)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { detailedPlayer ->
                    previewedPlayerDetails = detailedPlayer.toUi()
                },
                { error ->
                    // TODO: provide error handling
                }
            )
        )
    }
}
