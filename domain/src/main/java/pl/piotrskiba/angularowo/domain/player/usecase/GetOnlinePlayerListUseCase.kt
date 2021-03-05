package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.model.Rank
import pl.piotrskiba.angularowo.domain.rank.model.toNewRank
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetOnlinePlayerListUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository
) {

    fun execute(apiKey: String, accessToken: String): Single<List<Player>> {
        return playerRepository
            .getOnlinePlayerList(apiKey, accessToken)
            .concatMap { playerList ->
                rankRepository.getAllRanks().map { rankList ->
                    playerList.map { player ->
                        val rankName = (player.rank as Rank.UnknownRank).name
                        player.rank =
                            rankList.firstOrNull { it.name == rankName } ?: rankName.toNewRank()
                        player
                    }
                }
            }
    }
}
