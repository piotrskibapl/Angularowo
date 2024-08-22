package pl.piotrskiba.angularowo.domain.friend.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.analytics.repository.AnalyticsRepository
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.player.usecase.GetAppUserPlayerUseCase
import javax.inject.Inject

class UnmarkPlayerAsFavoriteUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
    private val analyticsRepository: AnalyticsRepository,
    private val getAppUserPlayerUseCase: GetAppUserPlayerUseCase,
) {

    fun execute(uuid: String, username: String): Completable {
        return friendRepository.delete(FriendModel(uuid))
            .andThen(
                getAppUserPlayerUseCase.execute(ignoreCache = false)
                    .flatMapCompletable { player -> analyticsRepository.logUnfavorite(player.uuid, player.username, uuid, username) },
            )
    }
}
