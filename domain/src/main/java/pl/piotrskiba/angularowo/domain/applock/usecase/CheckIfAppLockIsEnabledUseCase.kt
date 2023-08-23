package pl.piotrskiba.angularowo.domain.applock.usecase

import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository
import java.util.Calendar
import javax.inject.Inject

class CheckIfAppLockIsEnabledUseCase @Inject constructor(
    private val appLockRepository: AppLockRepository,
) {

    fun execute() =
        appLockRepository.getAppLockConfig()
            .map {
                val calendar = Calendar.getInstance()
                it.startTimestamp * 1000 <= calendar.timeInMillis && it.endTimestamp * 1000 > calendar.timeInMillis
            }
}
