package pl.piotrskiba.angularowo.domain.applock.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.applock.model.AppLockDataModel
import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class GetAppLockDataUseCase @Inject constructor(
    private val appLockRepository: AppLockRepository,
    private val playerRepository: PlayerRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        appLockRepository.getAppLockConfig()
            .flatMap { config ->
                checkIgnoreAppLockPermission()
                    .onErrorReturnItem(false)
                    .map { hasIgnoreAppLockPermission ->
                        AppLockDataModel(config, canSkip = hasIgnoreAppLockPermission)
                    }
            }

    private fun checkIgnoreAppLockPermission() =
        if (preferencesRepository.accessToken != null && preferencesRepository.uuid != null) {
            playerRepository.getPlayerDetailsFromUuid(preferencesRepository.accessToken!!, preferencesRepository.uuid!!)
                .map { it.permissions.contains(PermissionModel.IGNORE_APP_LOCK) }
        } else {
            Single.just(false)
        }
}
