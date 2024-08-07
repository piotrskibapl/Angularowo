package pl.piotrskiba.angularowo.domain.offers.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository

class RedeemPrizeOfferUseCaseTest {

    val offersRepository: OffersRepository = mockk()
    val tested = RedeemPrizeOfferUseCase(offersRepository)

    @Test
    fun `SHOULD redeem prize offer`() {
        val offerId = "id"
        every { offersRepository.redeemPrizeOffer(offerId) } returns Completable.complete()

        val result = tested.execute(offerId).test()

        result.assertComplete()
    }
}
