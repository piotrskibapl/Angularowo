package pl.piotrskiba.angularowo.main.player.list.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
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
import pl.piotrskiba.angularowo.domain.player.usecase.ObserveOnlinePlayerListWithFavoriteInformationUseCase
import pl.piotrskiba.angularowo.domain.player.usecase.RefreshOnlinePlayerListUseCase
import pl.piotrskiba.angularowo.main.player.list.nav.PlayerListNavigator
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import pl.piotrskiba.angularowo.main.player.model.toPlayerBannerData
import javax.inject.Inject

class PlayerListViewModel @Inject constructor(
    private val observeOnlinePlayerListWithFavoriteInformationUseCase: ObserveOnlinePlayerListWithFavoriteInformationUseCase,
    private val refreshOnlinePlayerListUseCase: RefreshOnlinePlayerListUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading)
    val players: ObservableList<PlayerBannerData> = ObservableArrayList()
    val favoritePlayers: ObservableList<PlayerBannerData> = ObservableArrayList()
    val playersBinding = ItemBinding.of<PlayerBannerData>(BR.player, R.layout.player_list_item)
    var isFavoritePlayerListNotEmpty = MutableLiveData(false)
    lateinit var navigator: PlayerListNavigator
    private val disposables = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }

    override fun onFirstCreate() {
        playersBinding.bindExtra(BR.navigator, navigator)
        observePlayerList()
        refreshPlayerList()
    }

    fun onRefresh() {
        refreshPlayerList()
    }

    private fun observePlayerList() {
        state.value = Loading
        disposables.add(observeOnlinePlayerListWithFavoriteInformationUseCase
            .execute()
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
                    // TODO: use DiffObservableList
                    players.clear()
                    favoritePlayers.clear()
                    players.addAll(bannerList)
                    favoritePlayers.addAll(favoriteBannerList)
                    isFavoritePlayerListNotEmpty.value = favoritePlayers.isNotEmpty()
                },
                { error ->
                    state.value = Error(error)
                }
            )
        )
    }

    private fun refreshPlayerList() {
        refreshOnlinePlayerListUseCase
            .execute(preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                {
                    state.value = Loaded
                },
                { error ->
                    state.value = Error(error)
                    // TODO: provide error handling
                }
            )
    }
}
