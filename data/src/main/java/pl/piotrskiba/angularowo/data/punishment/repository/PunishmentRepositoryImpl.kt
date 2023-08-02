package pl.piotrskiba.angularowo.data.punishment.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.punishment.PunishmentApiService
import pl.piotrskiba.angularowo.data.punishment.model.toDomain
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import pl.piotrskiba.angularowo.domain.punishment.model.toRemote
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository

class PunishmentRepositoryImpl(
    private val punishmentApi: PunishmentApiService,
) : PunishmentRepository {

    override fun getPunishments(
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilterModel,
    ): Single<List<PunishmentModel>> =
        punishmentApi
            .getPunishmentList(
                username = null,
                type = punishmentTypes.toRemote(),
                filter = filter.toRemote(),
            )
            .map { it.toDomain() }

    override fun getPlayerPunishments(
        username: String,
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilterModel,
    ): Single<List<PunishmentModel>> =
        punishmentApi
            .getPunishmentList(
                username = username,
                type = punishmentTypes.toRemote(),
                filter = filter.toRemote(),
            )
            .map { it.toDomain() }

    override fun mutePlayer(
        uuid: String,
        reason: String,
        length: Long,
    ): Completable =
        punishmentApi.mutePlayer(
            uuid = uuid,
            reason = reason,
            length = length,
        )

    override fun kickPlayer(uuid: String, reason: String): Completable =
        punishmentApi.kickPlayer(
            uuid = uuid,
            reason = reason,
        )

    override fun warnPlayer(uuid: String, reason: String, length: Long): Completable =
        punishmentApi.warnPlayer(
            uuid = uuid,
            reason = reason,
            length = length,
        )

    override fun banPlayer(uuid: String, reason: String, length: Long): Completable =
        punishmentApi.banPlayer(
            uuid = uuid,
            reason = reason,
            length = length,
        )
}
