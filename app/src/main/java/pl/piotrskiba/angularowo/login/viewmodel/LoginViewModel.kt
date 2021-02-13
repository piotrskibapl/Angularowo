package pl.piotrskiba.angularowo.login.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersFacade
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    val registerDeviceUseCase: RegisterDeviceUseCase,
    val facade: SchedulersProvider
) : ViewModel() {

    fun onPinEntered(pin: String) {
        registerDeviceUseCase
            .execute(BuildConfig.API_KEY, pin)
            .observeOn(facade.io())
            .subscribeOn(facade.ui())
            .subscribe { accessToken ->
                Log.d("asdasd", accessToken.username)
            }
    }
}
