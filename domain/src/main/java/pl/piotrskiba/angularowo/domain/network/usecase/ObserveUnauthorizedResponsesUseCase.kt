package pl.piotrskiba.angularowo.domain.network.usecase

import pl.piotrskiba.angularowo.domain.network.repository.UnauthorizedRepository
import javax.inject.Inject

class ObserveUnauthorizedResponsesUseCase @Inject constructor(
    private val unauthorizedRepository: UnauthorizedRepository,
) {

    fun execute() =
        unauthorizedRepository.observe()
}
