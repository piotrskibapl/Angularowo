package pl.piotrskiba.angularowo.data.friend.di

import android.content.Context
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.base.database.AppDatabase
import pl.piotrskiba.angularowo.data.friend.dao.FriendDao
import pl.piotrskiba.angularowo.data.friend.repository.FriendRepositoryImpl
import pl.piotrskiba.angularowo.domain.friend.repository.FriendRepository
import pl.piotrskiba.angularowo.domain.friend.usecase.MarkPlayerAsFavoriteUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.ObserveFavoritePlayerListUseCase
import pl.piotrskiba.angularowo.domain.friend.usecase.UnmarkPlayerAsFavoriteUseCase

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

    @Provides
    fun provideObserveFavoritePlayerListUseCase(friendRepository: FriendRepository): ObserveFavoritePlayerListUseCase =
        ObserveFavoritePlayerListUseCase(friendRepository)

    @Provides
    fun provideMarkPlayerAsFavoriteUseCase(friendRepository: FriendRepository): MarkPlayerAsFavoriteUseCase =
        MarkPlayerAsFavoriteUseCase(friendRepository)

    @Provides
    fun provideUnmarkPlayerAsFavoriteUseCase(friendRepository: FriendRepository): UnmarkPlayerAsFavoriteUseCase =
        UnmarkPlayerAsFavoriteUseCase(friendRepository)
}
