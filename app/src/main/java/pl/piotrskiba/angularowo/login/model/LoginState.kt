package pl.piotrskiba.angularowo.login.model

import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError

sealed class LoginState {
    data object Unknown : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val error: AccessTokenError) : LoginState()
}
