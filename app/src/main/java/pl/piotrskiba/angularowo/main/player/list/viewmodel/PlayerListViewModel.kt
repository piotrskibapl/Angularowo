package pl.piotrskiba.angularowo.main.player.list.viewmodel

import androidx.lifecycle.MutableLiveData
import com.snakydesign.livedataextensions.combineLatest
import com.snakydesign.livedataextensions.map
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.player.usecase.ObserveOnlinePlayerListWithFavoriteInformationUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.RefreshOnlinePlayerListUseCase
import pl.piotrskiba.angularowo.main.player.list.nav.PlayerListNavigator
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner
import pl.piotrskiba.angularowo.main.player.model.toPlayerBannerData
import javax.inject.Inject

class PlayerListViewModel @Inject constructor(
    private val observeOnlinePlayerListWithFavoriteInformationUseCase: ObserveOnlinePlayerListWithFavoriteInformationUseCase,
    private val refreshOnlinePlayerListUseCase: RefreshOnlinePlayerListUseCase,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val players: MutableLiveData<List<PlayerBanner>> = MutableLiveData()
    val favoritePlayers: MutableLiveData<List<PlayerBanner>> = MutableLiveData()
    val playersBinding = ItemBinding.of<PlayerBanner>(BR.player, R.layout.player_list_item)
    val isFavoritePlayerListNotEmpty = favoritePlayers.map { it.isNotEmpty() }
    val isNoPlayersOnline = combineLatest(players, favoritePlayers) { players, favoritePlayers ->
        players.isEmpty() && favoritePlayers.isEmpty()
    }
    lateinit var navigator: PlayerListNavigator

    override fun onFirstCreate() {
        playersBinding.bindExtra(BR.navigator, navigator)
        observePlayerList()
        refreshPlayerList()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        refreshPlayerList()
    }

    private fun observePlayerList() {
        disposables.add(
            observeOnlinePlayerListWithFavoriteInformationUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    { playerList ->
                        val bannerList = playerList
                            .filter { !it.second }
                            .map { it.first.toPlayerBannerData(false) }
                        val favoriteBannerList = playerList
                            .filter { it.second }
                            .map { it.first.toPlayerBannerData(true) }
                        state.value = Loaded
                        players.value = bannerList
                        favoritePlayers.value = favoriteBannerList
                    },
                    { error ->
                        state.value = Error(error)
                    }
                )
        )
    }

    private fun refreshPlayerList() {
        disposables.add(
            refreshOnlinePlayerListUseCase
                .execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    { },
                    { error ->
                        state.value = Error(error)
                    }
                )
        )
    }
}
