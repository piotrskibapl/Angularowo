package pl.piotrskiba.angularowo.domain.punishment.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository

class PunishPlayerUseCaseTest {

    val uuid = "uuid"
    val reason = "reason"
    val length = 123L
    val punishmentRepository: PunishmentRepository = mockk()
    val tested = PunishPlayerUseCase(punishmentRepository)

    @Test
    fun `SHOULD mute player WHEN punishment type is MUTE`() {
        every { punishmentRepository.mutePlayer(uuid, reason, length) } returns Completable.complete()

        val result = tested.execute(uuid, reason, PunishmentTypeModel.MUTE, length).test()

        result.assertComplete()
    }

    @Test
    fun `SHOULD kick player WHEN punishment type is KICK`() {
        every { punishmentRepository.kickPlayer(uuid, reason) } returns Completable.complete()

        val result = tested.execute(uuid, reason, PunishmentTypeModel.KICK, length).test()

        result.assertComplete()
    }

    @Test
    fun `SHOULD warn player WHEN punishment type is WARN`() {
        every { punishmentRepository.warnPlayer(uuid, reason, length) } returns Completable.complete()

        val result = tested.execute(uuid, reason, PunishmentTypeModel.WARN, length).test()

        result.assertComplete()
    }

    @Test
    fun `SHOULD ban player WHEN punishment type is BAN`() {
        every { punishmentRepository.banPlayer(uuid, reason, length) } returns Completable.complete()

        val result = tested.execute(uuid, reason, PunishmentTypeModel.BAN, length).test()

        result.assertComplete()
    }
}
