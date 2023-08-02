package pl.piotrskiba.angularowo.domain.offers.usecase

import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository
import javax.inject.Inject

class GetOffersInfoUseCase @Inject constructor(
    private val offersRepository: OffersRepository,
) {

    fun execute() =
        offersRepository.getOffersInfo()
}
