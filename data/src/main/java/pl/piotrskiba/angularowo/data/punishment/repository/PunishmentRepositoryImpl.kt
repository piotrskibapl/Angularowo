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
        accessToken: String,
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilterModel,
    ): Single<List<PunishmentModel>> =
        punishmentApi
            .getPunishmentList(
                accessToken = accessToken,
                username = null,
                type = punishmentTypes.toRemote(),
                filter = filter.toRemote(),
            )
            .map { it.toDomain() }

    override fun getPlayerPunishments(
        accessToken: String,
        username: String,
        punishmentTypes: List<PunishmentTypeModel>,
        filter: PunishmentFilterModel,
    ): Single<List<PunishmentModel>> =
        punishmentApi
            .getPunishmentList(
                accessToken = accessToken,
                username = username,
                type = punishmentTypes.toRemote(),
                filter = filter.toRemote(),
            )
            .map { it.toDomain() }

    override fun mutePlayer(
        accessToken: String,
        uuid: String,
        reason: String,
        length: Long,
    ): Completable =
        punishmentApi.mutePlayer(
            accessToken = accessToken,
            uuid = uuid,
            reason = reason,
            length = length,
        )

    override fun kickPlayer(accessToken: String, uuid: String, reason: String): Completable =
        punishmentApi.kickPlayer(
            accessToken = accessToken,
            uuid = uuid,
            reason = reason,
        )

    override fun warnPlayer(accessToken: String, uuid: String, reason: String, length: Long): Completable =
        punishmentApi.warnPlayer(
            accessToken = accessToken,
            uuid = uuid,
            reason = reason,
            length = length,
        )

    override fun banPlayer(accessToken: String, uuid: String, reason: String, length: Long): Completable =
        punishmentApi.banPlayer(
            accessToken = accessToken,
            uuid = uuid,
            reason = reason,
            length = length,
        )
}
