package pl.piotrskiba.angularowo.main.player.list.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.usecase.GetOnlinePlayerListUseCase
import pl.piotrskiba.angularowo.main.player.list.model.PlayerListState
import pl.piotrskiba.angularowo.main.player.model.toUi
import javax.inject.Inject

class PlayerListViewModel @Inject constructor(
    private val getOnlinePlayerListUseCase: GetOnlinePlayerListUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider
) : LifecycleViewModel() {

    val state = MutableLiveData<PlayerListState>(PlayerListState.Loading)

    override fun onFirstCreate() {
        getOnlinePlayerListUseCase
            .execute(BuildConfig.API_KEY, preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { playerList ->
                    val bannerList = playerList.map { it.toUi() }
                    state.value = PlayerListState.Loaded(bannerList)
                },
                { error ->
                    // TODO: provide error handling
                }
            )
    }
}