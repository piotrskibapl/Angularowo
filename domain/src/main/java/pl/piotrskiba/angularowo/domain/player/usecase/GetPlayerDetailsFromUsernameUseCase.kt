package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetPlayerDetailsFromUsernameUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository
) {

    fun execute(accessToken: String, username: String): Single<DetailedPlayer> {
        return playerRepository
            .getPlayerDetailsFromUsername(accessToken, username)
            .concatMap { player ->
                rankRepository.getAllRanks().map { rankList ->
                    player.rank = rankList.firstOrNull { it.name == player.rank.name } ?: player.rank
                    player
                }
            }
    }
}
