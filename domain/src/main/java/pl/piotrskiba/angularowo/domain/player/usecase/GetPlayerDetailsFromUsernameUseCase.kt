package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class GetPlayerDetailsFromUsernameUseCase @Inject constructor(private val playerRepository: PlayerRepository) {

    fun execute(apiKey: String, accessToken: String, username: String): Single<DetailedPlayer> {
        return playerRepository.getPlayerDetailsFromUsername(apiKey, accessToken, username)
    }
}
