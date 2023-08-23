package pl.piotrskiba.angularowo.domain.applock.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel
import pl.piotrskiba.angularowo.domain.applock.model.AppLockDataModel
import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.model.PermissionModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository

class GetAppLockDataUseCaseTest {

    val appLockRepository: AppLockRepository = mockk()
    val playerRepository: PlayerRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val tested = GetAppLockDataUseCase(appLockRepository, playerRepository, preferencesRepository)

    @Test
    fun `SHOULD get app lock data with canSkip set to false WHEN player has no ignore app lock permission`() {
        val appLockConfigModel: AppLockConfigModel = mockk()
        val uuid = "uuid"
        val detailedPlayerModel: DetailedPlayerModel = mockk {
            every { permissions } returns emptyList()
        }
        every { appLockRepository.getAppLockConfig() } returns Single.just(appLockConfigModel)
        every { preferencesRepository.uuid() } returns Maybe.just(uuid)
        every { playerRepository.getPlayerDetailsFromUuid(uuid) } returns Single.just(detailedPlayerModel)

        val result = tested.execute().test()

        result.assertValue(AppLockDataModel(appLockConfigModel, canSkip = false))
    }

    @Test
    fun `SHOULD get app lock data with canSkip set to true WHEN player has ignore app lock permission`() {
        val appLockConfigModel: AppLockConfigModel = mockk()
        val uuid = "uuid"
        val detailedPlayerModel: DetailedPlayerModel = mockk {
            every { permissions } returns listOf(PermissionModel.IGNORE_APP_LOCK)
        }
        every { appLockRepository.getAppLockConfig() } returns Single.just(appLockConfigModel)
        every { preferencesRepository.uuid() } returns Maybe.just(uuid)
        every { playerRepository.getPlayerDetailsFromUuid(uuid) } returns Single.just(detailedPlayerModel)

        val result = tested.execute().test()

        result.assertValue(AppLockDataModel(appLockConfigModel, canSkip = true))
    }

    @Test
    fun `SHOULD get app lock data with canSkip set to false WHEN player uuid is missing`() {
        val appLockConfigModel: AppLockConfigModel = mockk()
        every { appLockRepository.getAppLockConfig() } returns Single.just(appLockConfigModel)
        every { preferencesRepository.uuid() } returns Maybe.empty()

        val result = tested.execute().test()

        result.assertValue(AppLockDataModel(appLockConfigModel, canSkip = false))
    }

    @Test
    fun `SHOULD get app lock data with canSkip set to false WHEN fetching player details fails`() {
        val appLockConfigModel: AppLockConfigModel = mockk()
        val uuid = "uuid"
        every { appLockRepository.getAppLockConfig() } returns Single.just(appLockConfigModel)
        every { preferencesRepository.uuid() } returns Maybe.just(uuid)
        every { playerRepository.getPlayerDetailsFromUuid(uuid) } returns Single.error(Throwable())

        val result = tested.execute().test()

        result.assertValue(AppLockDataModel(appLockConfigModel, canSkip = false))
    }
}
