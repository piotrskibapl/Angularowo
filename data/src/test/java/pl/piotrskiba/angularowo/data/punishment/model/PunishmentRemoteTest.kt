package pl.piotrskiba.angularowo.data.punishment.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentModel
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel
import java.util.Date

class PunishmentRemoteTest {

    companion object {

        @JvmStatic
        fun parameters() = listOf(
            "ban" to PunishmentTypeModel.BAN,
            "mute" to PunishmentTypeModel.MUTE,
            "warn" to PunishmentTypeModel.WARN,
            "kick" to PunishmentTypeModel.KICK,
        ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map remote object to domain WHEN punishment type is {0}`(
        punishmentTypeRemote: String,
        punishmentType: PunishmentTypeModel,
    ) {
        val tested = PunishmentRemote(
            id = "id",
            name = "name",
            uuid = "uuid",
            reason = "reason",
            actor_name = "actor_name",
            start = 123L,
            end = 456L,
            type = punishmentTypeRemote,
        )

        tested.toDomain() shouldBeEqualTo PunishmentModel(
            id = "id",
            username = "name",
            uuid = "uuid",
            reason = "reason",
            actorName = "actor_name",
            created = Date(123L),
            expires = Date(456L),
            type = punishmentType,
        )
    }
}
