package pl.piotrskiba.angularowo.base.di

import android.app.Application
import android.content.Context
import dagger.Binds
import dagger.Module
import pl.piotrskiba.angularowo.base.rx.SchedulersFacade
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindSchedulersProvider(schedulersFacade: SchedulersFacade): SchedulersProvider
}