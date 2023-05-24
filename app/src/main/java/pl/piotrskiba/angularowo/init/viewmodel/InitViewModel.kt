package pl.piotrskiba.angularowo.init.viewmodel

import pl.piotrskiba.angularowo.base.rx.SchedulersFacade
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.applock.usecase.CheckIfAppLockIsEnabledUseCase
import pl.piotrskiba.angularowo.domain.login.usecase.CheckIfUserIsLoggedInUseCase
import pl.piotrskiba.angularowo.init.nav.InitNavigator
import javax.inject.Inject

class InitViewModel @Inject constructor(
    private val checkIfAppLockIsEnabledUseCase: CheckIfAppLockIsEnabledUseCase,
    private val checkIfUserIsLoggedInUseCase: CheckIfUserIsLoggedInUseCase,
    private val facade: SchedulersFacade,
) : LifecycleViewModel() {

    lateinit var navigator: InitNavigator

    override fun onFirstCreate() {
        checkIfAppLockIsEnabled()
    }

    private fun checkIfAppLockIsEnabled() {
        disposables.add(
            checkIfAppLockIsEnabledUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { enabled ->
                    if (enabled) {
                        navigator.displayAppLock()
                    } else {
                        checkIfUserIsLoggedIn()
                    }
                }
        )
    }

    private fun checkIfUserIsLoggedIn() {
        disposables.add(
            checkIfUserIsLoggedInUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { loggedIn ->
                    if (loggedIn) {
                        navigator.displayMainScreen()
                    } else {
                        navigator.displayLogin()
                    }
                }
        )
    }
}