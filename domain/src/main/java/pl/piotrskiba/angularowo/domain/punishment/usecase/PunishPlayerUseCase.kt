package pl.piotrskiba.angularowo.domain.punishment.usecase

import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import javax.inject.Inject

class PunishPlayerUseCase @Inject constructor(
    private val punishmentRepository: PunishmentRepository,
) {

    fun execute(uuid: String, reason: String, punishmentType: PunishmentTypeModel, length: Long?) =
        when (punishmentType) {
            PunishmentTypeModel.MUTE -> punishmentRepository.mutePlayer(uuid, reason, length!!)
            PunishmentTypeModel.KICK -> punishmentRepository.kickPlayer(uuid, reason)
            PunishmentTypeModel.WARN -> punishmentRepository.warnPlayer(uuid, reason, length!!)
            PunishmentTypeModel.BAN -> punishmentRepository.banPlayer(uuid, reason, length!!)
        }
}
