package pl.piotrskiba.angularowo.data.network.repository

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.data.network.interceptors.UnauthorizedInterceptor
import pl.piotrskiba.angularowo.domain.network.repository.NetworkRepository
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    private val unauthorizedInterceptor: UnauthorizedInterceptor,
) : NetworkRepository {

    override fun observeUnauthorizedResponses(): Observable<Unit> =
        unauthorizedInterceptor.unauthorizedResponses.hide()
}
