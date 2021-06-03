package pl.piotrskiba.angularowo.data.punishment.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.punishment.PunishmentApiService
import pl.piotrskiba.angularowo.data.punishment.model.toDomain
import pl.piotrskiba.angularowo.domain.punishment.model.Punishment
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilter
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentType
import pl.piotrskiba.angularowo.domain.punishment.repository.PunishmentRepository
import java.util.Locale
import javax.inject.Inject

class PunishmentRepositoryImpl @Inject constructor(
    private val punishmentApi: PunishmentApiService
) : PunishmentRepository {

    override fun getPunishments(
        apiKey: String,
        accessToken: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<Punishment>> =
        punishmentApi
            .getBanList(
                apiKey,
                accessToken,
                null,
                punishmentTypes.joinToString(
                    separator = ",",
                    transform = { it.toString().toLowerCase(Locale.ROOT) }
                ),
                filter.toString().toLowerCase(Locale.ROOT)
            )
            .map { it.toDomain() }

    override fun getPlayerPunishments(
        apiKey: String,
        accessToken: String,
        username: String,
        punishmentTypes: List<PunishmentType>,
        filter: PunishmentFilter
    ): Single<List<Punishment>> =
        punishmentApi
            .getBanList(
                apiKey,
                accessToken,
                username,
                punishmentTypes.joinToString(
                    separator = ",",
                    transform = { it.toString().toLowerCase(Locale.ROOT) }
                ),
                filter.toString().toLowerCase(Locale.ROOT)
            )
            .map { it.toDomain() }
}
