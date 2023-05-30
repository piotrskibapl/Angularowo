package pl.piotrskiba.angularowo.login.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val loginState = MutableLiveData<LoginState>(LoginState.Unknown)

    fun onPinEntered(pin: String) {
        loginState.value = LoginState.Loading
        disposables.add(
            registerDeviceUseCase.execute(pin)
                .applyDefaultSchedulers(facade)
                .subscribe(
                    { accessToken ->
                        AnalyticsUtils().logLogin(accessToken.uuid, accessToken.username)
                        loginState.value = LoginState.Success
                    },
                    { error ->
                        AnalyticsUtils().logLoginError(error::class.simpleName)
                        loginState.value = LoginState.Error(error as AccessTokenError)
                    }
                )
        )
    }
}
