package pl.piotrskiba.angularowo.data.rank.model

import org.amshove.kluent.assertSoftly
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.rank.model.RankModel

class RankRemoteTest {

    @Test
    fun `SHOULD map remote object to domain`() {
        val tested = RankRemote(
            name = "admin",
            staff = true,
            colorCode = "4",
            chatColorCode = "c",
        )
        val expected = RankModel(
            name = "admin",
            staff = true,
            colorCode = "4",
            chatColorCode = "c",
        )

        assertSoftly {
            tested.toDomain() shouldBeEqualTo expected
            listOf(tested).toDomain() shouldBeEqualTo listOf(expected)
        }
    }
}
