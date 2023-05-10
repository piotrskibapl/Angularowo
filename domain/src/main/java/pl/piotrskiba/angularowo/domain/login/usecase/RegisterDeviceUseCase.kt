package pl.piotrskiba.angularowo.domain.login.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenModel
import pl.piotrskiba.angularowo.domain.login.repository.LoginRepository
import javax.inject.Inject

class RegisterDeviceUseCase @Inject constructor(
    private val loginRepository: LoginRepository,
    private val preferencesRepository: PreferencesRepository,
    private val cloudMessagingRepository: CloudMessagingRepository,
) {

    fun execute(userCode: String) =
        loginRepository.registerDevice(userCode)
            .flatMap { accessToken ->
                cloudMessagingRepository.subscribeToPlayerUuid(accessToken.uuid)
                    .mergeWith(saveUserData(accessToken))
                    .toSingleDefault(accessToken)
            }

    private fun saveUserData(accessToken: AccessTokenModel) =
        Completable.fromAction {
            preferencesRepository.uuid = accessToken.uuid
            preferencesRepository.username = accessToken.username
            preferencesRepository.accessToken = accessToken.accessToken
        }
}
