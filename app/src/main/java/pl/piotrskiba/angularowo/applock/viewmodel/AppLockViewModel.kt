package pl.piotrskiba.angularowo.applock.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.applock.model.AppLockData
import pl.piotrskiba.angularowo.applock.model.toUi
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

    override fun onFirstCreate() {
        disposables.add(
            getAppLockDataUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { data ->
                    appLockData.value = data.config.toUi()
                    state.value = Loaded
                }
        )
    }
}
