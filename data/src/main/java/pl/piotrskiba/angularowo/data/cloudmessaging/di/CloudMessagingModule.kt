package pl.piotrskiba.angularowo.data.cloudmessaging.di

import com.google.firebase.messaging.FirebaseMessaging
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.cloudmessaging.repository.CloudMessagingRepositoryImpl
import pl.piotrskiba.angularowo.data.firebase.di.FirebaseModule
import pl.piotrskiba.angularowo.domain.cloudmessaging.repository.CloudMessagingRepository

@Module(
    includes = [FirebaseModule::class]
)
class CloudMessagingModule {

    @Provides
    fun provideCloudMessagingRepository(firebaseMessaging: FirebaseMessaging): CloudMessagingRepository =
        CloudMessagingRepositoryImpl(firebaseMessaging)
}