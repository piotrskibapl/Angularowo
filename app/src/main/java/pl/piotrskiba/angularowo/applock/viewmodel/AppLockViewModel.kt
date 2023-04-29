package pl.piotrskiba.angularowo.applock.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.applock.model.AppLockData
import pl.piotrskiba.angularowo.applock.model.toUi
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.applock.usecase.GetAppLockConfigUseCase
import javax.inject.Inject

class AppLockViewModel @Inject constructor(
    private val getAppLockConfigUseCase: GetAppLockConfigUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val appLockData = MutableLiveData<AppLockData>()

    fun loadData() {
        if (appLockData.value == null) {
            disposables.add(
                getAppLockConfigUseCase.execute()
                    .subscribeOn(facade.io())
                    .observeOn(facade.ui())
                    .subscribe { config ->
                        appLockData.value = config.toUi()
                    }
            )
        }
    }
}
