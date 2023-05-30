package pl.piotrskiba.angularowo.data.server.repository

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.data.BuildConfig
import pl.piotrskiba.angularowo.data.server.ServerApiService
import pl.piotrskiba.angularowo.data.server.model.toDomain
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository

class ServerRepositoryImpl(
    private val serverApi: ServerApiService,
) : ServerRepository {

    override fun getServerStatus(
        accessToken: String,
    ): Single<ServerStatusModel> =
        serverApi
            .getServerStatus(BuildConfig.API_KEY, accessToken)
            .map { it.toDomain() }
}
