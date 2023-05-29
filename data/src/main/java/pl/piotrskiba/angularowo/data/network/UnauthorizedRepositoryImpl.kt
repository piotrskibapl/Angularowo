package pl.piotrskiba.angularowo.data.network

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.data.network.interceptors.UnauthorizedInterceptor
import pl.piotrskiba.angularowo.domain.network.repository.UnauthorizedRepository
import javax.inject.Inject

class UnauthorizedRepositoryImpl @Inject constructor(
    private val unauthorizedInterceptor: UnauthorizedInterceptor,
) : UnauthorizedRepository {

    override fun observe(): Observable<Unit> =
        unauthorizedInterceptor.unauthorizedResponses.hide()
}
