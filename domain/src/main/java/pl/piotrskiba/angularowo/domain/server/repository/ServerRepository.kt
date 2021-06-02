package pl.piotrskiba.angularowo.domain.server.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.server.model.ServerStatus

interface ServerRepository {

    fun getServerStatus(
        apiKey: String,
        accessToken: String
    ): Single<ServerStatus>
}
