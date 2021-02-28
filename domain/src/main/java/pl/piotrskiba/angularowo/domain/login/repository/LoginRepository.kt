package pl.piotrskiba.angularowo.domain.login.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.login.model.AccessToken

interface LoginRepository {

    fun registerDevice(apiKey: String, userCode: String): Single<AccessToken>
}