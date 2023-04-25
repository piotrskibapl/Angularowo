package pl.piotrskiba.angularowo.data.punishment.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.punishment.PunishmentApiService
import pl.piotrskiba.angularowo.data.punishment.model.toDomain
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.domain.punishment.model.toRemote
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository

class PunishmentRepositoryImpl(
    private val punishmentApi: PunishmentApiService
) : PunishmentRepository {

    override fun getPunishments(
        accessToken: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<PunishmentModel>> =
        punishmentApi
            .getPunishmentList(
                BuildConfig.API_KEY,
                accessToken,
                null,
                punishmentTypes.toRemote(),
                filter.toRemote()
            )
            .map { it.toDomain() }

    override fun getPlayerPunishments(
        accessToken: String,
        username: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<PunishmentModel>> =
        punishmentApi
            .getPunishmentList(
                BuildConfig.API_KEY,
                accessToken,
                username,
                punishmentTypes.toRemote(),
                filter.toRemote()
            )
            .map { it.toDomain() }
}
