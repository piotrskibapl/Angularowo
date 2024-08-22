package pl.piotrskiba.angularowo.data.firebase.di

import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.analytics.repository.AnalyticsRepositoryImpl
import pl.piotrskiba.angularowo.domain.analytics.repository.AnalyticsRepository
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseMessaging(): FirebaseMessaging = FirebaseMessaging.getInstance()

    @Singleton
    @Provides
    fun provideFirebaseAnalytics(): FirebaseAnalytics = Firebase.analytics

    @Singleton
    @Provides
    fun provideAnalyticsRepository(firebaseAnalytics: FirebaseAnalytics): AnalyticsRepository = AnalyticsRepositoryImpl(firebaseAnalytics)
}
