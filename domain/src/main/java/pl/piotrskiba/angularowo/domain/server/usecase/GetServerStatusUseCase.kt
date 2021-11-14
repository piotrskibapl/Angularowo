package pl.piotrskiba.angularowo.domain.server.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository
import javax.inject.Inject

class GetServerStatusUseCase @Inject constructor(
    private val serverRepository: ServerRepository
) {

    fun execute(accessToken: String): Single<ServerStatusModel> {
        return serverRepository
            .getServerStatus(accessToken)
    }
}
