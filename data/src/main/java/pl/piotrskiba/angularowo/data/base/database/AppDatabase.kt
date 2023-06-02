package pl.piotrskiba.angularowo.data.base.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.piotrskiba.angularowo.data.friend.dao.FriendDao
import pl.piotrskiba.angularowo.data.friend.model.FriendEntity

private const val DATABASE_NAME = "Angularowo"

@Database(entities = [FriendEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun friendDao(): FriendDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}