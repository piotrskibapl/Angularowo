package pl.piotrskiba.angularowo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import pl.piotrskiba.angularowo.database.dao.FriendDao
import pl.piotrskiba.angularowo.database.entity.Friend

private const val DATABASE_NAME = "Angularowo"

@Database(entities = [Friend::class], version = 1)
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