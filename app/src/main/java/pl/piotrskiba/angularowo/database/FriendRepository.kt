package pl.piotrskiba.angularowo.database

import androidx.lifecycle.LiveData
import pl.piotrskiba.angularowo.database.dao.FriendDao
import pl.piotrskiba.angularowo.database.entity.Friend

class FriendRepository(private val friendDao: FriendDao) {

    val all: LiveData<List<Friend>> = friendDao.getAll()

    suspend fun insert(friend: Friend) {
        friendDao.insert(friend)
    }

    suspend fun delete(friend: Friend) {
        friendDao.delete(friend)
    }
}