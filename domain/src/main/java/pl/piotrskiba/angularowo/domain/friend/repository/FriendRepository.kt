package pl.piotrskiba.angularowo.domain.friend.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel

interface FriendRepository {

    fun getAllFriends(): Observable<List<FriendModel>>

    fun insert(friend: FriendModel): Completable

    fun delete(friend: FriendModel): Completable
}