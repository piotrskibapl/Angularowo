package pl.piotrskiba.angularowo.domain.base.preferences.usecase

import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository
import javax.inject.Inject

class CheckIfIsStaffUserUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val rankRepository: RankRepository,
) {

    fun execute() =
        preferencesRepository.rankName()
            .toSingle()
            .zipWith(rankRepository.getAllRanks(), ::Pair)
            .map { (rankName, ranks) ->
                ranks.find { it.name == rankName }?.staff ?: false
            }
}
