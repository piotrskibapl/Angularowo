package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.login.ui.LoginActivity
import pl.piotrskiba.angularowo.main.base.ui.MainActivity
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsActivity

@Module(includes = [ViewModelModule::class])
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindPlayerDetailsActivity(): PlayerDetailsActivity
}
