package pl.piotrskiba.angularowo.base.di

import android.app.Application
import android.content.Context
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.base.rx.SchedulersFacade
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider

@Module
abstract class AppModule {

    @Binds
    abstract fun bindContext(application: Application): Context

    @Binds
    abstract fun bindSchedulersProvider(schedulersFacade: SchedulersFacade): SchedulersProvider

    companion object {

        @Provides
        fun provideAppUpdateManager(context: Context): AppUpdateManager =
            AppUpdateManagerFactory.create(context)
    }
}
