package pl.piotrskiba.angularowo.domain.punishment.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel.ACTIVE
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel.ALL
import pl.piotrskiba.angularowo.domain.punishment.model.PunishmentFilterModel.EXPIRED

class PunishmentFilterModelTest {

    companion object {

        @JvmStatic
        fun parameters() =
            listOf(
                ACTIVE to "active",
                EXPIRED to "expired",
                ALL to "all",
            ).map { Arguments.of(it.first, it.second) }
    }

    @ParameterizedTest
    @MethodSource("parameters")
    fun `SHOULD map domain model to remote`(punishmentFilterModel: PunishmentFilterModel, expected: String) {
        punishmentFilterModel.toRemote() shouldBeEqualTo expected
    }
}
