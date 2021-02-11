package pl.piotrskiba.angularowo.base.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.piotrskiba.angularowo.data.login.di.LoginApiModule
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        LoginApiModule::class,
        AppModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<Application> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
