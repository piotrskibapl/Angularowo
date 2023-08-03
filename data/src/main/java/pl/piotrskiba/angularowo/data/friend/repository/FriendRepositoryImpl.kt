package pl.piotrskiba.angularowo.data.friend.repository

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.data.friend.dao.FriendDao
import pl.piotrskiba.angularowo.data.friend.model.toDomain
import pl.piotrskiba.angularowo.data.friend.model.toRemote
import pl.piotrskiba.angularowo.domain.friend.model.FriendModel
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository

class FriendRepositoryImpl(
    private val friendDao: FriendDao,
) : FriendRepository {

    override fun getAllFriends(): Observable<List<FriendModel>> =
        friendDao.getAllFriends().map { it.toDomain() }

    override fun insert(friend: FriendModel): Completable =
        friendDao.insert(friend.toRemote())

    override fun delete(friend: FriendModel): Completable =
        friendDao.delete(friend.toRemote())
}
