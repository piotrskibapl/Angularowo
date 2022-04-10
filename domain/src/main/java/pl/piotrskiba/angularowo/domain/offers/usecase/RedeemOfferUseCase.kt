package pl.piotrskiba.angularowo.domain.offers.usecase

import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class RedeemOfferUseCase @Inject constructor(
    private val offersRepository: OffersRepository,
) {

    fun execute(accessToken: String, offerId: String) =
        offersRepository.redeemOffer(accessToken, offerId)
}
