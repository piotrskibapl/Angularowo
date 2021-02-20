package pl.piotrskiba.angularowo.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import pl.piotrskiba.angularowo.login.model.LoginState
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val facade: SchedulersProvider
) : ViewModel() {

    var loginState = MutableLiveData<LoginState>(LoginState.Unknown)

    fun onPinEntered(pin: String) {
        loginState.value = LoginState.Loading
        registerDeviceUseCase
            .execute(BuildConfig.API_KEY, pin)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { accessToken ->
                    loginState.value = LoginState.Success(accessToken)
                },
                { error ->
                    loginState.value = LoginState.Error
                }
            )
    }
}
