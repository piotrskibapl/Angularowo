package pl.piotrskiba.angularowo.data.applock.di

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.applock.repository.AppLockRepositoryImpl
import pl.piotrskiba.angularowo.data.firebase.di.FirebaseModule
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.domain.applock.repository.AppLockRepository

@Module(
    includes = [
        NetworkModule::class,
        FirebaseModule::class,
    ],
)
class AppLockModule {

    @Provides
    fun provideAppLockRepository(
        firebaseRemoteConfig: FirebaseRemoteConfig,
    ): AppLockRepository =
        AppLockRepositoryImpl(firebaseRemoteConfig)
}
