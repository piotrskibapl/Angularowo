package pl.piotrskiba.angularowo.domain.login.usecase

import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val cloudMessagingRepository: CloudMessagingRepository,
    ) {

    fun execute(userCode: String) =
        loginRepository.registerDevice(userCode)
            .flatMap { accessToken ->
                cloudMessagingRepository.subscribeToPlayerUuid(accessToken.uuid)
                    .toSingleDefault(accessToken)
            }
}
