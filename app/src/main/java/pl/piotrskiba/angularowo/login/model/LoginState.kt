package pl.piotrskiba.angularowo.login.model

import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError

sealed class LoginState {
    object Unknown : LoginState()
    object Loading : LoginState()
    data class Error(val error: AccessTokenError) : LoginState()
    data class Success(val accessToken: AccessTokenModel) : LoginState()
}
