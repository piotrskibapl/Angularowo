package pl.piotrskiba.angularowo.base.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.piotrskiba.angularowo.base.AngularowoApplication
import pl.piotrskiba.angularowo.data.firebase.di.FirebaseModule
import pl.piotrskiba.angularowo.data.login.di.LoginModule
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.player.di.PlayerModule
import pl.piotrskiba.angularowo.data.rank.di.RankModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        FirebaseModule::class,
        LoginModule::class,
        PlayerModule::class,
        RankModule::class,
        AppModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<AngularowoApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
