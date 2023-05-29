package pl.piotrskiba.angularowo.domain.network.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.network.repository.UnauthorizedRepository
import pl.piotrskiba.angularowo.domain.settings.usecase.LogoutUserUseCase
import javax.inject.Inject

class ObserveUnauthorizedResponsesUseCase @Inject constructor(
    private val unauthorizedRepository: UnauthorizedRepository,
    private val logoutUserUseCase: LogoutUserUseCase,
) {

    fun execute(appVersionCode: Int) =
        unauthorizedRepository.observe()
            .flatMap {
                logoutUserUseCase.execute(appVersionCode)
                    .andThen(Observable.just(Unit))
            }
}
