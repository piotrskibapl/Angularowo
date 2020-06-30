package pl.piotrskiba.angularowo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.piotrskiba.angularowo.database.dao.FriendDao
import pl.piotrskiba.angularowo.database.entity.Friend

@Database(entities = [Friend::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun friendDao(): FriendDao
}