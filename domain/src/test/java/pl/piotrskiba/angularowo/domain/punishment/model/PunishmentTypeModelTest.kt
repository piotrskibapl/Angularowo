package pl.piotrskiba.angularowo.domain.punishment.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.BAN
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.KICK
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.MUTE
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentTypeModel.WARN

class PunishmentTypeModelTest {

    companion object {

        @JvmStatic
        fun parameters() =
            listOf(
                listOf(BAN) to "ban",
                listOf(MUTE) to "mute",
                listOf(WARN) to "warn",
                listOf(KICK) to "kick",
                listOf(BAN, KICK) to "ban,kick",
                listOf(BAN, WARN, KICK, MUTE) to "ban,warn,kick,mute",
            ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map domain model to remote`(punishmentTypeModels: List<PunishmentTypeModel>, expected: String) {
        punishmentTypeModels.toRemote() shouldBeEqualTo expected
    }
}
