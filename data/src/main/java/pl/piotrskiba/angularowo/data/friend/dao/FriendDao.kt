package pl.piotrskiba.angularowo.data.friend.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import pl.piotrskiba.angularowo.data.friend.model.FriendEntity

@Dao
interface FriendDao {

    @Query("SELECT * FROM friends")
    fun getAllFriends(): Observable<List<FriendEntity>>

    @Insert
    fun insert(friend: FriendEntity): Completable

    @Delete
    fun delete(friend: FriendEntity): Completable
}
