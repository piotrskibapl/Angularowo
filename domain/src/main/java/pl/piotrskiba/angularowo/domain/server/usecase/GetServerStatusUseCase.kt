package pl.piotrskiba.angularowo.domain.server.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.server.model.ServerStatusModel
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository
import javax.inject.Inject

class GetServerStatusUseCase @Inject constructor(
    private val serverRepository: ServerRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(): Single<ServerStatusModel> {
        return serverRepository
            .getServerStatus(preferencesRepository.accessToken!!)
    }
}
