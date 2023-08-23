package pl.piotrskiba.angularowo.domain.offers.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository

class RedeemOfferUseCaseTest {

    val offersRepository: OffersRepository = mockk()
    val tested = RedeemOfferUseCase(offersRepository)

    @Test
    fun `SHOULD redeem offer`() {
        val offerId = "id"
        every { offersRepository.redeemOffer(offerId) } returns Completable.complete()

        val result = tested.execute(offerId).test()

        result.assertComplete()
    }
}
