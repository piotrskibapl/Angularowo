package pl.piotrskiba.angularowo.domain.punishment.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.punishment.model.Punishment
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import javax.inject.Inject

class GetActivePlayerPunishmentsUseCase @Inject constructor(
    private val punishmentRepository: PunishmentRepository
) {

    fun execute(apiKey: String, accessToken: String, username: String): Single<List<Punishment>> {
        return punishmentRepository
            .getPlayerPunishments(
                apiKey,
                accessToken,
                username,
                listOf(PunishmentType.MUTE, PunishmentType.WARN, PunishmentType.BAN),
                PunishmentFilter.ACTIVE
            )
    }
}
