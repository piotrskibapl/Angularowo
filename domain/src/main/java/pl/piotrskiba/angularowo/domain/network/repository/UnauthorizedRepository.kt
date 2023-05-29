package pl.piotrskiba.angularowo.domain.network.repository

import io.reactivex.rxjava3.core.Observable

interface UnauthorizedRepository {

    fun observe(): Observable<Unit>
}
