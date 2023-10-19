package pl.piotrskiba.angularowo.applock.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.applock.model.AppLockData
import pl.piotrskiba.angularowo.applock.model.toUi
import pl.piotrskiba.angularowo.applock.nav.AppLockNavigator
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.applock.usecase.GetAppLockDataUseCase
import javax.inject.Inject

class AppLockViewModel @Inject constructor(
    private val getAppLockDataUseCase: GetAppLockDataUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val appLockData = MutableLiveData<AppLockData>()
    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    lateinit var navigator: AppLockNavigator

    override fun onFirstCreate() {
        disposables.add(
            getAppLockDataUseCase.execute()
                .applyDefaultSchedulers(facade)
                .subscribe { data ->
                    appLockData.value = data.toUi()
                    state.value = Loaded
                },
        )
    }

    fun onBackPressed() {
        if (appLockData.value!!.canSkip) {
            navigator.navigateToMainScreen()
        } else {
            navigator.closeApp()
        }
    }
}
