package pl.piotrskiba.angularowo.data.login.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.login.model.toDomain
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import pl.piotrskiba.angularowo.domain.login.model.toAccessTokenError
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository

class LoginRepositoryImpl(
    private val loginApi: LoginApiService
) : LoginRepository {

    override fun registerDevice(userCode: String): Single<AccessTokenModel> =
        loginApi
            .registerDevice(BuildConfig.API_KEY, userCode)
            .map { it.toDomain() }
            .onErrorResumeNext { Single.error(it.toAccessTokenError()) }
}
