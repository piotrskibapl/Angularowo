package pl.piotrskiba.angularowo.domain.mainscreen.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.mainscreen.model.MainScreenDataModel
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.usecase.GetAppUserPlayerUseCase
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel.ACTIVE
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.BAN
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.MUTE
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.WARN
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository

class GetMainScreenDataAndSavePlayerUseCaseTest {

    val serverRepository: ServerRepository = mockk()
    val punishmentRepository: PunishmentRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val getAppUserPlayerUseCase: GetAppUserPlayerUseCase = mockk()
    val tested = GetMainScreenDataAndSavePlayerUseCase(serverRepository, punishmentRepository, preferencesRepository, getAppUserPlayerUseCase)

    @Test
    fun `SHOULD return main screen data model`() {
        val apiUsername = "username"
        val apiRankName = "rank"
        val serverStatusModel: ServerStatusModel = mockk()
        val detailedPlayerModel: DetailedPlayerModel = mockk {
            every { username } returns apiUsername
            every { rank.name } returns apiRankName
        }
        val punishmentModels: List<PunishmentModel> = mockk()
        every { getAppUserPlayerUseCase.execute(ignoreCache = true) } returns Single.just(detailedPlayerModel)
        every { serverRepository.getServerStatus() } returns Single.just(serverStatusModel)
        every { preferencesRepository.setUsername(apiUsername) } returns Completable.complete()
        every { preferencesRepository.setRankName(apiRankName) } returns Completable.complete()
        every { punishmentRepository.getPlayerPunishments(apiUsername, listOf(MUTE, WARN, BAN), ACTIVE) } returns Single.just(punishmentModels)

        val result = tested.execute().test()

        assertSoftly {
            result.assertValue(
                MainScreenDataModel(
                    serverStatusModel = serverStatusModel,
                    detailedPlayerModel = detailedPlayerModel,
                    playerPunishments = punishmentModels,
                ),
            )
            verify { preferencesRepository.setUsername(apiUsername) }
            verify { preferencesRepository.setRankName(apiRankName) }
        }
    }
}
