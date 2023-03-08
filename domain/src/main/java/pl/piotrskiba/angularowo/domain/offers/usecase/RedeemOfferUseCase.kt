package pl.piotrskiba.angularowo.domain.offers.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class RedeemOfferUseCase @Inject constructor(
    private val offersRepository: OffersRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(offerId: String) =
        offersRepository.redeemOffer(preferencesRepository.accessToken!!, offerId)
}
