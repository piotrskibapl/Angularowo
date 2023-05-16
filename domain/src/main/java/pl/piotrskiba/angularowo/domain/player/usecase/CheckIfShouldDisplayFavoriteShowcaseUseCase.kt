package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Single
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import javax.inject.Inject

class CheckIfShouldDisplayFavoriteShowcaseUseCase @Inject constructor(
    val preferencesRepository: PreferencesRepository,
) {

    fun execute() =
        preferencesRepository.hasSeenFavoriteShowcase()
            .flatMap { hasSeenFavoriteShowcase ->
                if (!hasSeenFavoriteShowcase) {
                    preferencesRepository.setHasSeenFavoriteShowcase(true)
                        .toSingleDefault(true)
                } else {
                    Single.just(false)
                }
            }
}
