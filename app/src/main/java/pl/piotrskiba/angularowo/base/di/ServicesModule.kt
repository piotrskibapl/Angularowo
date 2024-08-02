package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.base.AngularowoFirebaseMessagingService

@Module
abstract class ServicesModule {

    @ContributesAndroidInjector
    abstract fun bindFirebaseMessagingService(): AngularowoFirebaseMessagingService
}
