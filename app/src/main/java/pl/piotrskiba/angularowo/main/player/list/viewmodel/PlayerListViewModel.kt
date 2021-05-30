package pl.piotrskiba.angularowo.main.player.list.viewmodel

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.usecase.GetOnlinePlayerListUseCase
import pl.piotrskiba.angularowo.main.player.list.model.PlayerListState
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import pl.piotrskiba.angularowo.main.player.model.toUi
import javax.inject.Inject

class PlayerListViewModel @Inject constructor(
    private val getOnlinePlayerListUseCase: GetOnlinePlayerListUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val state = MutableLiveData<PlayerListState>(PlayerListState.Loading)
    val players: ObservableList<PlayerBannerData> = ObservableArrayList()
    val playersBinding = ItemBinding.of<PlayerBannerData>(BR.player, R.layout.player_list_item)

    init {
        playersBinding.bindExtra(BR.viewModel, this)
    }

    override fun onFirstCreate() {
        getOnlinePlayerListUseCase
            .execute(BuildConfig.API_KEY, preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { playerList ->
                    Log.d("asdasd", playerList.toString())
                    val bannerList = playerList.map { it.toUi() }
                    state.value = PlayerListState.Loaded(bannerList)
                    // TODO: use DiffObservableList
                    players.clear()
                    players.addAll(bannerList)
                },
                { error ->
                    Log.d("asdasd", error.toString())
                    // TODO: provide error handling
                }
            )
    }

    fun onPlayerClick(player: PlayerBannerData) {
    }
}