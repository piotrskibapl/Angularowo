package pl.piotrskiba.angularowo.data.punishment.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import pl.piotrskiba.angularowo.data.punishment.PunishmentApiService
import pl.piotrskiba.angularowo.data.punishment.model.PunishmentRemote
import pl.piotrskiba.angularowo.data.punishment.model.toDomain
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.model.toRemote

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PunishmentRepositoryImplTest {

    val punishmentApi: PunishmentApiService = mockk()
    val tested = PunishmentRepositoryImpl(punishmentApi)

    @BeforeAll
    fun setup() {
        mockkStatic(
            List<PunishmentTypeModel>::toRemote,
            PunishmentFilterModel::toRemote,
            List<PunishmentRemote>::toDomain,
        )
    }

    @Test
    fun `SHOULD get punishments`() {
        val punishmentTypesRemote = "kick"
        val punishmentTypes: List<PunishmentTypeModel> = mockk {
            every { toRemote() } returns punishmentTypesRemote
        }
        val punishmentFilterRemote = "expired"
        val punishmentFilter: PunishmentFilterModel = mockk {
            every { toRemote() } returns punishmentFilterRemote
        }
        val punishments: List<PunishmentModel> = mockk()
        val punishmentsRemote: List<PunishmentRemote> = mockk {
            every { toDomain() } returns punishments
        }
        every { punishmentApi.getPunishmentList(username = null, punishmentTypesRemote, punishmentFilterRemote) } returns Single.just(punishmentsRemote)

        val result = tested.getPunishments(punishmentTypes, punishmentFilter).test()

        result.assertValue(punishments)
    }

    @Test
    fun `SHOULD get player punishments`() {
        val username = "username"
        val punishmentTypesRemote = "kick"
        val punishmentTypes: List<PunishmentTypeModel> = mockk {
            every { toRemote() } returns punishmentTypesRemote
        }
        val punishmentFilterRemote = "expired"
        val punishmentFilter: PunishmentFilterModel = mockk {
            every { toRemote() } returns punishmentFilterRemote
        }
        val punishments: List<PunishmentModel> = mockk()
        val punishmentsRemote: List<PunishmentRemote> = mockk {
            every { toDomain() } returns punishments
        }
        every { punishmentApi.getPunishmentList(username, punishmentTypesRemote, punishmentFilterRemote) } returns Single.just(punishmentsRemote)

        val result = tested.getPlayerPunishments(username, punishmentTypes, punishmentFilter).test()

        result.assertValue(punishments)
    }

    @Test
    fun `SHOULD mute player`() {
        every { punishmentApi.mutePlayer("uuid", "reason", 123L) } returns Completable.complete()

        val result = tested.mutePlayer("uuid", "reason", 123L).test()

        result.assertComplete()
    }

    @Test
    fun `SHOULD kick player`() {
        every { punishmentApi.kickPlayer("uuid", "reason") } returns Completable.complete()

        val result = tested.kickPlayer("uuid", "reason").test()

        result.assertComplete()
    }

    @Test
    fun `SHOULD warn player`() {
        every { punishmentApi.warnPlayer("uuid", "reason", 123L) } returns Completable.complete()

        val result = tested.warnPlayer("uuid", "reason", 123L).test()

        result.assertComplete()
    }

    @Test
    fun `SHOULD ban player`() {
        every { punishmentApi.banPlayer("uuid", "reason", 123L) } returns Completable.complete()

        val result = tested.banPlayer("uuid", "reason", 123L).test()

        result.assertComplete()
    }
}
