package pl.piotrskiba.angularowo.data.offers.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.model.AdOfferModel
import java.util.Date

class AdOfferRemoteTest {

    @Test
    fun `SHOUL map remote object to domain`() {
        val tested = listOf(
            AdOfferRemote(
                id = "id",
                points = 999,
                adId = "adId",
                timeBreak = 888,
                availabilityDate = 777L,
            ),
        )

        tested.toDomain() shouldBeEqualTo listOf(
            AdOfferModel(
                id = "id",
                points = 999,
                adId = "adId",
                timeBreak = 888,
                availabilityDate = Date(777L),
            ),
        )
    }
}
