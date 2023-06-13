package pl.piotrskiba.angularowo.data.rank.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.firebase.di.FirebaseModule
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.rank.repository.RankRepositoryImpl
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository

@Module(
    includes = [
        NetworkModule::class,
        FirebaseModule::class,
    ],
)
class RankModule {

    @Provides
    fun provideRankRepository(
        firebaseRemoteConfig: FirebaseRemoteConfig,
        gson: Gson,
    ): RankRepository =
        RankRepositoryImpl(firebaseRemoteConfig, gson)
}
