package pl.piotrskiba.angularowo.domain.login.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.login.model.AccessToken
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(private val loginRepository: LoginRepository) {

    fun execute(apiKey: String, userCode: String): Single<AccessToken> {
        return loginRepository.registerDevice(apiKey, userCode)
    }
}