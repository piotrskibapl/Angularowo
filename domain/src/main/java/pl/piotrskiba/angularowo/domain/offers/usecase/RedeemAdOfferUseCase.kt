package pl.piotrskiba.angularowo.domain.offers.usecase

import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class RedeemAdOfferUseCase @Inject constructor(
    private val offersRepository: OffersRepository,
) {

    fun execute(adOfferId: String) =
        offersRepository.redeemAdOffer(adOfferId)
}
