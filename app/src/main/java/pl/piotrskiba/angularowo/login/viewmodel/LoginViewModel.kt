package pl.piotrskiba.angularowo.login.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import pl.piotrskiba.angularowo.login.model.LoginState
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
                        loginState.value = LoginState.Success
                    },
                    { error ->
                        loginState.value = LoginState.Error(error as AccessTokenError)
                    },
                ),
        )
    }
}
