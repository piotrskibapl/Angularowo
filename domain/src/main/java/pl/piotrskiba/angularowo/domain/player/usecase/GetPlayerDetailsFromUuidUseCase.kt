package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class GetPlayerDetailsFromUuidUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val rankRepository: RankRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(uuid: String): Single<DetailedPlayerModel> {
        return playerRepository
            .getPlayerDetailsFromUuid(preferencesRepository.accessToken!!, uuid)
            .concatMap { player ->
                rankRepository.getAllRanks().map { rankList ->
                    player.rank = rankList.firstOrNull { it.name == player.rank.name } ?: player.rank
                    player
                }
            }
    }
}
