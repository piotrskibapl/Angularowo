package pl.piotrskiba.angularowo.domain.server.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel

interface ServerRepository {

    fun getServerStatus(
        accessToken: String,
    ): Single<ServerStatusModel>
}
