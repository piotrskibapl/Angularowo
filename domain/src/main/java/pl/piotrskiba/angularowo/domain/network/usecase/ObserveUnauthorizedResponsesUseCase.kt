package pl.piotrskiba.angularowo.domain.network.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.network.repository.NetworkRepository
import pl.piotrskiba.angularowo.domain.settings.usecase.LogoutUserUseCase
import javax.inject.Inject

class ObserveUnauthorizedResponsesUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val logoutUserUseCase: LogoutUserUseCase,
) {

    fun execute(appVersionCode: Int) =
        networkRepository.observeUnauthorizedResponses()
            .flatMap {
                logoutUserUseCase.execute(appVersionCode)
                    .andThen(Observable.just(Unit))
            }
}
