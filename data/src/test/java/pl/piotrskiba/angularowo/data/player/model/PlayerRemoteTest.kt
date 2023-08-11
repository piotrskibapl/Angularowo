package pl.piotrskiba.angularowo.data.player.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

class PlayerRemoteTest {

    @Test
    fun `SHOULD map remote object to domain`() {
        val tested = listOf(
            PlayerRemote(
                uuid = "uuid",
                skinUuid = "skinUuid",
                partnerUuid = "partnerUuid",
                username = "username",
                rank = "rank",
                vanished = false,
            ),
        )

        tested.toDomain() shouldBeEqualTo listOf(
            PlayerModel(
                uuid = "uuid",
                skinUuid = "skinUuid",
                partnerUuid = "partnerUuid",
                username = "username",
                rank = RankModel.unknownRank("rank"),
                isVanished = false,
            ),
        )
    }
}
