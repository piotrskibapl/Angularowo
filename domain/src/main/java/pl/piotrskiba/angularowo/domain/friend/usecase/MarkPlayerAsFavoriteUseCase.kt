package pl.piotrskiba.angularowo.domain.friend.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.friend.model.Friend
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import javax.inject.Inject

class MarkPlayerAsFavoriteUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {

    fun execute(friend: Friend): Completable {
        return friendRepository.insert(friend)
    }
}
