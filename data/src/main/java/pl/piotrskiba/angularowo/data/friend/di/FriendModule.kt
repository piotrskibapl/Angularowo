package pl.piotrskiba.angularowo.data.friend.di

import android.content.Context
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.base.database.AppDatabase
import pl.piotrskiba.angularowo.data.friend.dao.FriendDao
import pl.piotrskiba.angularowo.data.friend.repository.FriendRepositoryImpl
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository

@Module
class FriendModule {

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideFriendDao(appDatabase: AppDatabase): FriendDao =
        appDatabase.friendDao()

    @Provides
    fun provideFriendRepository(friendDao: FriendDao): FriendRepository =
        FriendRepositoryImpl(friendDao)
}
