package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
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
                        player.rank = rankList.firstOrNull { it.name == player.rank.name } ?: player.rank
                        player
                    }
                }
            }
            .concatMap { playerList ->
                val sortedPlayerList: MutableList<Player> = mutableListOf()
                rankRepository.getAllRanks().map { rankList ->
                    rankList.forEach { rank ->
                        sortedPlayerList.addAll(playerList.filter { it.rank.name == rank.name })
                    }
                    sortedPlayerList
                }
            }
    }
}
