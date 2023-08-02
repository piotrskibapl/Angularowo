package pl.piotrskiba.angularowo.domain.player.usecase

import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetPlayerDetailsFromUuidUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository,
) {

    fun execute(uuid: String) =
        playerRepository.getPlayerDetailsFromUuid(uuid)
            .flatMap { player ->
                rankRepository.getAllRanks()
                    .map { ranks ->
                        val playerRank = ranks.firstOrNull { it.name == player.rank.name } ?: player.rank
                        player.copy(rank = playerRank)
                    }
            }
}
