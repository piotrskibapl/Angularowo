package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.player.model.PlayerModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class ObserveOnlinePlayerListWithFavoriteInformationUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository,
    private val friendRepository: FriendRepository,
) {

    fun execute(): Observable<List<Pair<PlayerModel, Boolean>>> =
        rankRepository
            .getAllRanks()
            .flatMapObservable { rankList ->
                friendRepository
                    .getAllFriends()
                    .flatMap { friendList ->
                        playerRepository
                            .observeOnlinePlayerList()
                            .map { playerList ->
                                playerList.map { player ->
                                    val updatedRank = rankList.firstOrNull { it.name == player.rank.name } ?: player.rank
                                    player.copy(rank = updatedRank)
                                }
                            }
                            .map { playerList ->
                                val sortedPlayerList: MutableList<PlayerModel> = mutableListOf()
                                rankList.forEach { rank ->
                                    sortedPlayerList.addAll(playerList.filter { it.rank.name == rank.name })
                                }
                                sortedPlayerList
                            }
                            .map { playerList ->
                                playerList.map { player ->
                                    Pair(
                                        player,
                                        friendList
                                            .map { friend -> friend.uuid }
                                            .contains(player.uuid),
                                    )
                                }
                            }
                    }
            }
}
