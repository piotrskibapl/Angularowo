package pl.piotrskiba.angularowo.data.login.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.model.toDomain
import pl.piotrskiba.angularowo.domain.login.model.AccessToken
import pl.piotrskiba.angularowo.domain.login.model.toAccessTokenError
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(
    private val loginApi: LoginApiService
) : LoginRepository {

    override fun registerDevice(apiKey: String, userCode: String): Single<AccessToken> =
        loginApi
            .registerDevice(apiKey, userCode)
            .map { it.toDomain() }
            .onErrorResumeNext { Single.error(it.toAccessTokenError()) }
}
