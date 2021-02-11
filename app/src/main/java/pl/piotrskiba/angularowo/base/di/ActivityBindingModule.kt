package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.login.LoginActivity

@Module(includes = [ViewModelModule::class])
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainScreenActivity(): LoginActivity
}
