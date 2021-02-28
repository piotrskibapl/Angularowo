package pl.piotrskiba.angularowo.login.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.login.model.AccessToken
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val facade: SchedulersProvider,
    private val preferencesRepository: PreferencesRepository
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
                    saveUserData(accessToken)
                    AnalyticsUtils().logLogin(accessToken.uuid, accessToken.username)
                    loginState.value = LoginState.Success(accessToken)
                },
                { error ->
                    AnalyticsUtils().logLoginError(error.message)
                    loginState.value = LoginState.Error
                }
            )
    }

    private fun saveUserData(accessToken: AccessToken) {
        preferencesRepository.uuid = accessToken.uuid
        preferencesRepository.username = accessToken.username
        preferencesRepository.accessToken = accessToken.accessToken
    }
}
