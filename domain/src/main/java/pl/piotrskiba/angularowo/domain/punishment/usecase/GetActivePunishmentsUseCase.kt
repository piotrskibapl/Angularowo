package pl.piotrskiba.angularowo.domain.punishment.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import javax.inject.Inject

class GetActivePunishmentsUseCase @Inject constructor(
    private val punishmentRepository: PunishmentRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(): Single<List<PunishmentModel>> {
        return punishmentRepository
            .getPunishments(
                preferencesRepository.accessToken!!,
                listOf(PunishmentType.MUTE, PunishmentType.WARN, PunishmentType.BAN),
                PunishmentFilter.ACTIVE
            )
    }
}
