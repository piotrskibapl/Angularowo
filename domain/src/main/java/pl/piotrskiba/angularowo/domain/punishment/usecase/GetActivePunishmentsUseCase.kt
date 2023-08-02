package pl.piotrskiba.angularowo.domain.punishment.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import javax.inject.Inject

class GetActivePunishmentsUseCase @Inject constructor(
    private val punishmentRepository: PunishmentRepository,
) {

    fun execute(): Single<List<PunishmentModel>> =
        punishmentRepository.getPunishments(
            punishmentTypes = listOf(
                PunishmentTypeModel.MUTE,
                PunishmentTypeModel.WARN,
                PunishmentTypeModel.BAN,
            ),
            filter = PunishmentFilterModel.ACTIVE,
        )
}
