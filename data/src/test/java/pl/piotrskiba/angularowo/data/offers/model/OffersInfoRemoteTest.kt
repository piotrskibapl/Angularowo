package pl.piotrskiba.angularowo.data.offers.model

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.model.AdOfferModel
import pl.piotrskiba.angularowo.domain.offers.model.OfferModel
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel

class OffersInfoRemoteTest {

    @BeforeEach
    fun setup() {
        mockkStatic(
            List<AdOfferRemote>::toDomain,
            List<OfferRemote>::toDomain,
        )
    }

    @AfterEach
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOUL map remote object to domain`() {
        val adOffers: List<AdOfferModel> = mockk()
        val adOffersRemote: List<AdOfferRemote> = mockk {
            every { toDomain() } returns adOffers
        }
        val offers: List<OfferModel> = mockk()
        val offersRemote: List<OfferRemote> = mockk {
            every { toDomain() } returns offers
        }
        val tested = OffersInfoRemote(
            points = 999,
            pointsLimitReached = true,
            adOffers = adOffersRemote,
            offers = offersRemote,
        )

        tested.toDomain() shouldBeEqualTo OffersInfoModel(
            points = 999,
            pointsLimitReached = true,
            adOffers = adOffers,
            offers = offers,
        )
    }
}
