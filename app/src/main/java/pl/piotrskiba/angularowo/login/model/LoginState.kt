package pl.piotrskiba.angularowo.login.model

import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError

sealed class LoginState {
    object Unknown : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val error: AccessTokenError) : LoginState()
}
