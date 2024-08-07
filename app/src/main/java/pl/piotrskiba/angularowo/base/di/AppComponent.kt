package pl.piotrskiba.angularowo.base.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.piotrskiba.angularowo.base.AngularowoApplication
import pl.piotrskiba.angularowo.data.applock.di.AppLockModule
import pl.piotrskiba.angularowo.data.chat.di.ChatModule
import pl.piotrskiba.angularowo.data.cloudmessaging.di.CloudMessagingModule
import pl.piotrskiba.angularowo.data.firebase.di.FirebaseModule
import pl.piotrskiba.angularowo.data.friend.di.FriendModule
import pl.piotrskiba.angularowo.data.login.di.LoginModule
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.offers.di.OffersModule
import pl.piotrskiba.angularowo.data.player.di.PlayerModule
import pl.piotrskiba.angularowo.data.punishment.di.PunishmentModule
import pl.piotrskiba.angularowo.data.rank.di.RankModule
import pl.piotrskiba.angularowo.data.report.di.ReportModule
import pl.piotrskiba.angularowo.data.server.di.ServerModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        FirebaseModule::class,
        CloudMessagingModule::class,
        ServerModule::class,
        LoginModule::class,
        PlayerModule::class,
        FriendModule::class,
        RankModule::class,
        PunishmentModule::class,
        ReportModule::class,
        OffersModule::class,
        ChatModule::class,
        AppModule::class,
        AndroidSupportInjectionModule::class,
        AndroidInjectionModule::class,
        ActivityBindingModule::class,
        FragmentBindingModule::class,
        AppLockModule::class,
        ServicesModule::class,
    ],
)
interface AppComponent : AndroidInjector<AngularowoApplication> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
