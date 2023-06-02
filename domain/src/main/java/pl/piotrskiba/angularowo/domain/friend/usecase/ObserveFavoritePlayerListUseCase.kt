package pl.piotrskiba.angularowo.domain.friend.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import javax.inject.Inject

class ObserveFavoritePlayerListUseCase @Inject constructor(
    private val friendRepository: FriendRepository,
) {

    fun execute(): Observable<List<FriendModel>> {
        return friendRepository.getAllFriends()
    }
}
