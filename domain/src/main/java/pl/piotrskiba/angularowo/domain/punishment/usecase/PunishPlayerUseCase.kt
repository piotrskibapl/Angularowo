package pl.piotrskiba.angularowo.domain.punishment.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import javax.inject.Inject

class PunishPlayerUseCase @Inject constructor(
    private val punishmentRepository: PunishmentRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(uuid: String, reason: String, punishmentType: PunishmentTypeModel, length: Long?) =
        preferencesRepository.accessToken()
            .toSingle()
            .flatMapCompletable { accessToken ->
                when (punishmentType) {
                    PunishmentTypeModel.MUTE -> punishmentRepository.mutePlayer(accessToken, uuid, reason, length!!)
                    PunishmentTypeModel.KICK -> punishmentRepository.kickPlayer(accessToken, uuid, reason)
                    PunishmentTypeModel.WARN -> punishmentRepository.warnPlayer(accessToken, uuid, reason, length!!)
                    PunishmentTypeModel.BAN -> punishmentRepository.banPlayer(accessToken, uuid, reason, length!!)
                }
            }
}
