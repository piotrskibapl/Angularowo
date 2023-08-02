package pl.piotrskiba.angularowo.domain.offers.usecase

import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class RedeemOfferUseCase @Inject constructor(
    private val offersRepository: OffersRepository,
) {

    fun execute(offerId: String) =
        offersRepository.redeemOffer(offerId)
}
