package pl.piotrskiba.angularowo.login.model

import pl.piotrskiba.angularowo.domain.login.model.AccessToken

sealed class LoginState {
    object Unknown: LoginState()
    object Loading : LoginState()
    object Error : LoginState()
    data class Success(val accessToken: AccessToken) : LoginState()
}
