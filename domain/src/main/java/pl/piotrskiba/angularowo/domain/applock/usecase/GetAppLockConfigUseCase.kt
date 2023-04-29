package pl.piotrskiba.angularowo.domain.applock.usecase

import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository
import javax.inject.Inject

class GetAppLockConfigUseCase @Inject constructor(
    private val appLockRepository: AppLockRepository,
) {

    fun execute() =
        appLockRepository.getAppLockConfig()
}
