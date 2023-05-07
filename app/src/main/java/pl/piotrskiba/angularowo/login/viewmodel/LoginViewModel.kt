package pl.piotrskiba.angularowo.login.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.domain.login.usecase.RegisterDeviceUseCase
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    private val registerDeviceUseCase: RegisterDeviceUseCase,
    private val facade: SchedulersProvider,
    private val preferencesRepository: PreferencesRepository
) : LifecycleViewModel() {

    var loginState = MutableLiveData<LoginState>(LoginState.Unknown)

    fun onPinEntered(pin: String) {
        loginState.value = LoginState.Loading
        disposables.add(
            registerDeviceUseCase
                .execute(pin)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe(
                    { accessToken ->
                        saveUserData(accessToken)
                        AnalyticsUtils().logLogin(accessToken.uuid, accessToken.username)
                        loginState.value = LoginState.Success(accessToken)
                    },
                    { error ->
                        AnalyticsUtils().logLoginError(error::class.simpleName)
                        loginState.value = LoginState.Error(error as AccessTokenError)
                    }
                )
        )
    }

    // TODO: saving user data should be moved to usecase
    private fun saveUserData(accessTokenModel: AccessTokenModel) {
        preferencesRepository.uuid = accessTokenModel.uuid
        preferencesRepository.username = accessTokenModel.username
        preferencesRepository.accessToken = accessTokenModel.accessToken
    }
}
