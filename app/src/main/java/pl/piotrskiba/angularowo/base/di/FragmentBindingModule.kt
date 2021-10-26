package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.main.ban.list.ui.BanListFragment
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment

@Module(includes = [ViewModelModule::class])
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainScreenFragment(): MainScreenFragment

    @ContributesAndroidInjector
    abstract fun bindPlayerListFragment(): PlayerListFragment

    @ContributesAndroidInjector
    abstract fun bindPlayerDetailsFragment(): PlayerDetailsFragment

    @ContributesAndroidInjector
    abstract fun bindBanListFragment(): BanListFragment
}
