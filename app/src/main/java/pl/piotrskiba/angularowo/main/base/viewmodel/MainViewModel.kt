package pl.piotrskiba.angularowo.main.base.viewmodel

import androidx.lifecycle.MutableLiveData
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

    // TODO: player should be dropped from main view model
    val player = MutableLiveData<DetailedPlayerModel>()
    lateinit var navigator: MainNavigator
    private var appLockLoaded = false

    // TODO: display loading state
    fun loadData() {
        if (!appLockLoaded) {
            disposables.add(
                checkIfAppLockIsEnabledUseCase.execute()
                    .subscribeOn(facade.io())
                    .observeOn(facade.ui())
                    .subscribe { enabled ->
                        appLockLoaded = true
                        if (enabled) {
                            navigator.displayAppLock()
                        }
                    }
            )
        }
    }
}
