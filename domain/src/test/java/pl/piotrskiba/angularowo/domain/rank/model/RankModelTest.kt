package pl.piotrskiba.angularowo.domain.rank.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test

class RankModelTest {

    @Test
    fun `SHOULD create RankModel with staff set to false and colorCode and chatColorCode set to 7 WHEN unknownRank called`() {
        RankModel.unknownRank("name") shouldBeEqualTo RankModel(name = "name", staff = false, colorCode = "7", chatColorCode = "7")
    }
}
