package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.model.Rank.CustomRank
import pl.piotrskiba.angularowo.domain.rank.model.Rank.UnknownRank
import pl.piotrskiba.angularowo.domain.rank.model.toNewRank
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetPlayerDetailsFromUsernameUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository
) {

    fun execute(apiKey: String, accessToken: String, username: String): Single<DetailedPlayer> {
        return playerRepository
            .getPlayerDetailsFromUsername(apiKey, accessToken, username)
            .concatMap { player ->
                val rankName = (player.rank as UnknownRank).name
                rankRepository.getAllRanks().map { rankList ->
                    player.rank =
                        rankList.firstOrNull { it.name == rankName } ?: rankName.toNewRank()
                    player
                }
            }
    }
}
