package pl.piotrskiba.angularowo.domain.player.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetPlayerDetailsFromUsernameUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(username: String) =
        preferencesRepository.accessToken()
            .toSingle()
            .flatMap { accessToken ->
                playerRepository.getPlayerDetailsFromUsername(accessToken, username)
                    .flatMap { player ->
                        rankRepository.getAllRanks().map { ranks ->
                            val playerRank = ranks.firstOrNull { it.name == player.rank.name } ?: player.rank
                            player.copy(rank = playerRank)
                        }
                    }
            }
}
