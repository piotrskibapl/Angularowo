package pl.piotrskiba.angularowo.domain.player.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.player.repository.PlayerRepository
import javax.inject.Inject

class RefreshOnlinePlayerListUseCase @Inject constructor(
    private val playerRepository: PlayerRepository,
    private val preferencesRepository: PreferencesRepository,
) {

    fun execute(): Completable =
        playerRepository.refreshOnlinePlayerList(preferencesRepository.accessToken!!)
}
