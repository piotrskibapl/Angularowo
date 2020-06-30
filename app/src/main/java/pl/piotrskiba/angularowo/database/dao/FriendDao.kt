package pl.piotrskiba.angularowo.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import pl.piotrskiba.angularowo.database.entity.Friend

@Dao
interface FriendDao {

    @Query("SELECT * FROM friend")
    fun getAll(): List<Friend>

    @Insert
    fun insert(friend: Friend)

    @Delete
    fun delete(friend: Friend)
}