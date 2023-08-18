package pl.piotrskiba.angularowo.domain.offers.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Completable
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository

class RedeemAdOfferUseCaseTest {

    val offersRepository: OffersRepository = mockk()
    val tested = RedeemAdOfferUseCase(offersRepository)

    @Test
    fun `SHOULD redeem ad offer`() {
        val adOfferId = "id"
        every { offersRepository.redeemAdOffer(adOfferId) } returns Completable.complete()

        val result = tested.execute(adOfferId).test()

        result.assertComplete()
    }
}
