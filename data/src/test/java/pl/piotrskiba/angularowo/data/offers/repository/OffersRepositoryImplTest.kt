package pl.piotrskiba.angularowo.data.offers.repository

import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import io.mockk.verify
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.amshove.kluent.assertSoftly
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.piotrskiba.angularowo.data.offers.OffersApiService
import pl.piotrskiba.angularowo.data.offers.model.OffersInfoRemote
import pl.piotrskiba.angularowo.data.offers.model.toDomain
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel

class OffersRepositoryImplTest {

    val offersApi: OffersApiService = mockk()
    val tested = OffersRepositoryImpl(offersApi)

    @Before
    fun setup() {
        mockkStatic(OffersInfoRemote::toDomain)
    }

    @After
    fun teardown() {
        unmockkAll()
    }

    @Test
    fun `SHOULD get offers info`() {
        val offersInfo: OffersInfoModel = mockk()
        val offersInfoRemote: OffersInfoRemote = mockk {
            every { toDomain() } returns offersInfo
        }
        every { offersApi.getOffersInfo() } returns Single.just(offersInfoRemote)

        val result = tested.getOffersInfo().test()

        result.assertValue(offersInfo)
    }

    @Test
    fun `SHOULD redeem ad offer`() {
        val offerId = "offerId"
        every { offersApi.redeemAdOffer(offerId) } returns Completable.complete()

        val result = tested.redeemAdOffer(offerId).test()

        assertSoftly {
            verify { offersApi.redeemAdOffer(offerId) }
            result.assertComplete()
        }
    }

    @Test
    fun `SHOULD redeem offer`() {
        val offerId = "offerId"
        every { offersApi.redeemOffer(offerId) } returns Completable.complete()

        val result = tested.redeemOffer(offerId).test()

        assertSoftly {
            verify { offersApi.redeemOffer(offerId) }
            result.assertComplete()
        }
    }
}
