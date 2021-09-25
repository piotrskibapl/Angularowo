package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.player.model.Player
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class ObserveOnlinePlayerListUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository
) {

    fun execute(): Observable<List<Player>> =
        rankRepository
            .getAllRanks()
            .flatMapObservable { rankList ->
                playerRepository
                    .observeOnlinePlayerList()
                    .map { playerList ->
                        playerList.map { player ->
                            player.rank = rankList.firstOrNull { it.name == player.rank.name } ?: player.rank
                            player
                        }
                    }
                    .map { playerList ->
                        val sortedPlayerList: MutableList<Player> = mutableListOf()
                        rankList.forEach { rank ->
                            sortedPlayerList.addAll(playerList.filter { it.rank.name == rank.name })
                        }
                        sortedPlayerList
                    }
            }
}
