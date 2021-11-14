package pl.piotrskiba.angularowo.domain.friend.usecase

import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import javax.inject.Inject

class MarkPlayerAsFavoriteUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {

    fun execute(uuid: String): Completable {
        return friendRepository.insert(FriendModel(uuid))
    }
}
