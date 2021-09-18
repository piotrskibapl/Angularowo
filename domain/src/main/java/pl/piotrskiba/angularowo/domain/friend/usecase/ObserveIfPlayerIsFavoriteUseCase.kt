package pl.piotrskiba.angularowo.domain.friend.usecase

import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import javax.inject.Inject

class ObserveIfPlayerIsFavoriteUseCase @Inject constructor(
    private val friendRepository: FriendRepository
) {

    fun execute(uuid: String): Observable<Boolean> {
        return friendRepository
            .getAllFriends()
            .map { friendList ->
                friendList.find { it.uuid == uuid } != null
            }
    }
}