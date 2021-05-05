package pl.piotrskiba.angularowo.domain.friend.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.domain.friend.model.Friend

interface FriendRepository {

    fun getAllFriends(): Observable<List<Friend>>

    fun insert(friend: Friend): Completable

    fun delete(friend: Friend): Completable
}