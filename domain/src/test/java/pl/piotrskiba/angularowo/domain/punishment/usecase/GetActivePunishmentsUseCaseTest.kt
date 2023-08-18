package pl.piotrskiba.angularowo.domain.punishment.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository

class GetActivePunishmentsUseCaseTest {

    val punishmentRepository: PunishmentRepository = mockk()
    val tested = GetActivePunishmentsUseCase(punishmentRepository)

    @Test
    fun `SHOULD get active punishments`() {
        val punishmentTypes = listOf(
            PunishmentTypeModel.MUTE,
            PunishmentTypeModel.WARN,
            PunishmentTypeModel.BAN,
        )
        val punishmentFilter = PunishmentFilterModel.ACTIVE
        val punishmentModels: List<PunishmentModel> = mockk()
        every { punishmentRepository.getPunishments(punishmentTypes, punishmentFilter) } returns Single.just(punishmentModels)

        val result = tested.execute().test()

        result.assertValue(punishmentModels)
    }
}
