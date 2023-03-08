package pl.piotrskiba.angularowo.domain.offers.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class RedeemAdOfferUseCase @Inject constructor(
    private val offersRepository: OffersRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(adOfferId: String) =
        offersRepository.redeemAdOffer(preferencesRepository.accessToken!!, adOfferId)
}
