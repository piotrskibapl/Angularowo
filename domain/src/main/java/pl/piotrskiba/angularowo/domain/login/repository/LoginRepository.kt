package pl.piotrskiba.angularowo.domain.login.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel

interface LoginRepository {

    fun registerDevice(userCode: String): Single<AccessTokenModel>
}
