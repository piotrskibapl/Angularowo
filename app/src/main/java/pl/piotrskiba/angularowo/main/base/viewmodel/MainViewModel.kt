package pl.piotrskiba.angularowo.main.base.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersFacade
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.applock.usecase.CheckIfAppLockIsEnabledUseCase
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.base.nav.MainNavigator
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val checkIfAppLockIsEnabledUseCase: CheckIfAppLockIsEnabledUseCase,
    private val facade: SchedulersFacade,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val player = MutableLiveData<DetailedPlayerModel>() // TODO: player should be dropped from main view model
    lateinit var navigator: MainNavigator

    override fun onFirstCreate() {
        disposables.add(
            checkIfAppLockIsEnabledUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    { enabled ->
                        state.value = Loaded
                        if (enabled) {
                            navigator.displayAppLock()
                        }
                    },
                    { error ->
                        state.value = Error(error)
                    }
                )
        )
    }
}
