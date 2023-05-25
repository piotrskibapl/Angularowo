package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.main.base.ui.MainActivity

@Module(includes = [ViewModelModule::class])
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity
}
