package pl.piotrskiba.angularowo.domain.friend.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.friend.model.Friend
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import javax.inject.Inject

class UnmarkPlayerAsFavoriteUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {

    fun execute(uuid: String): Completable {
        return friendRepository.delete(Friend(uuid))
    }
}
