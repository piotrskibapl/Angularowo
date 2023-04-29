package pl.piotrskiba.angularowo.domain.applock.usecase

import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository
import javax.inject.Inject

class CheckIfAppLockIsEnabledUseCase @Inject constructor(
    private val appLockRepository: AppLockRepository,
) {

    fun execute() =
        appLockRepository.getAppLockConfig()
            .map { it.startTimestamp * 1000 <= System.currentTimeMillis() && it.endTimestamp * 1000 > System.currentTimeMillis() }
}
