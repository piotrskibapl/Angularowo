package pl.piotrskiba.angularowo.data.offers.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.model.OfferModel
import java.util.Date

class OfferRemoteTest {

    @Test
    fun `SHOUL map remote object to domain`() {
        val tested = listOf(
            OfferRemote(
                id = "id",
                title = "title",
                description = "description",
                price = 999,
                imageUrl = "url",
                timeBreak = 888,
                availabilityDate = 777L,
            ),
        )

        tested.toDomain() shouldBeEqualTo listOf(
            OfferModel(
                id = "id",
                title = "title",
                description = "description",
                price = 999,
                imageUrl = "url",
                timeBreak = 888,
                availabilityDate = Date(777L),
            ),
        )
    }
}
