package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetAppUserPlayerUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository,
) {

    private lateinit var cachedPlayer: DetailedPlayerModel

    fun execute(ignoreCache: Boolean) =
        if (!ignoreCache && this::cachedPlayer.isInitialized) {
            Single.just(cachedPlayer)
        } else {
            preferencesRepository.uuid()
                .toSingle()
                .flatMap { playerRepository.getPlayerDetailsFromUuid(it) }
                .zipWith(rankRepository.getAllRanks()) { player, ranks ->
                    val playerRank = ranks.firstOrNull { it.name == player.rank.name } ?: player.rank
                    player.copy(rank = playerRank)
                }
                .doAfterSuccess { cachedPlayer = it }
        }
}
