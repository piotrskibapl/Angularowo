package pl.piotrskiba.angularowo.data.friend.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.data.friend.dao.FriendDao
import pl.piotrskiba.angularowo.data.friend.model.toData
import pl.piotrskiba.angularowo.data.friend.model.toDomain
import pl.piotrskiba.angularowo.domain.friend.model.Friend
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import javax.inject.Inject

class FriendRepositoryImpl @Inject constructor(
    private val friendDao: FriendDao
) : FriendRepository {

    override fun getAllFriends(): Observable<List<Friend>> =
        friendDao.getAllFriends().map { it.toDomain() }

    override fun insert(friend: Friend): Completable =
        friendDao.insert(friend.toData())

    override fun delete(friend: Friend): Completable =
        friendDao.delete(friend.toData())
}