package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class RefreshOnlinePlayerListUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
) {

    fun execute(apiKey: String, accessToken: String): Completable =
        playerRepository.refreshOnlinePlayerList(apiKey, accessToken)
}
