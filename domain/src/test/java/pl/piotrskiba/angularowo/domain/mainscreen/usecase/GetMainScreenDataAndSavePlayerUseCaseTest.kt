package pl.piotrskiba.angularowo.domain.mainscreen.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.assertSoftly
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.mainscreen.model.MainScreenDataModel
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel.ACTIVE
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.BAN
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.MUTE
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.WARN
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository

class GetMainScreenDataAndSavePlayerUseCaseTest {

    val serverRepository: ServerRepository = mockk()
    val playerRepository: PlayerRepository = mockk()
    val rankRepository: RankRepository = mockk()
    val punishmentRepository: PunishmentRepository = mockk()
    val preferencesRepository: PreferencesRepository = mockk()
    val tested = GetMainScreenDataAndSavePlayerUseCase(serverRepository, playerRepository, rankRepository, punishmentRepository, preferencesRepository)

    @Test
    fun `SHOULD return main screen data model`() {
        val preferencesUuid = "uuid"
        val preferencesUsername = "username"
        val apiUsername = "username2"
        val apiRankName = "rank"
        val apiRank: RankModel = mockk {
            every { name } returns apiRankName
        }
        val preferencesRanks: List<RankModel> = listOf(
            mockk {
                every { name } returns apiRankName
            },
        )
        val serverStatusModel: ServerStatusModel = mockk()
        val updatedDetailedPlayerModel: DetailedPlayerModel = mockk()
        val detailedPlayerModel: DetailedPlayerModel = mockk {
            every { username } returns apiUsername
            every { rank } returns apiRank
            every { copy(rank = preferencesRanks.first()) } returns updatedDetailedPlayerModel
        }
        val punishmentModels: List<PunishmentModel> = mockk()
        every { preferencesRepository.uuid() } returns Maybe.just(preferencesUuid)
        every { preferencesRepository.username() } returns Maybe.just(preferencesUsername)
        every { serverRepository.getServerStatus() } returns Single.just(serverStatusModel)
        every { playerRepository.getPlayerDetailsFromUuid(preferencesUuid) } returns Single.just(detailedPlayerModel)
        every { preferencesRepository.setUsername(apiUsername) } returns Completable.complete()
        every { preferencesRepository.setRankName(apiRankName) } returns Completable.complete()
        every { rankRepository.getAllRanks() } returns Single.just(preferencesRanks)
        every { punishmentRepository.getPlayerPunishments(preferencesUsername, listOf(MUTE, WARN, BAN), ACTIVE) } returns Single.just(punishmentModels)

        val result = tested.execute().test()

        assertSoftly {
            result.assertValue(
                MainScreenDataModel(
                    serverStatusModel = serverStatusModel,
                    detailedPlayerModel = updatedDetailedPlayerModel,
                    playerPunishments = punishmentModels,
                ),
            )
            verify { preferencesRepository.setUsername(apiUsername) }
            verify { preferencesRepository.setRankName(apiRankName) }
        }
    }

    @Test
    fun `SHOULD return error WHEN uuid is empty`() {
        every { preferencesRepository.uuid() } returns Maybe.empty()
        every { preferencesRepository.username() } returns Maybe.just("username")

        val result = tested.execute().test()

        result.assertError(NoSuchElementException::class.java)
    }

    @Test
    fun `SHOULD return error WHEN username is empty`() {
        every { preferencesRepository.uuid() } returns Maybe.just("uuid")
        every { preferencesRepository.username() } returns Maybe.empty()

        val result = tested.execute().test()

        result.assertError(NoSuchElementException::class.java)
    }
}
