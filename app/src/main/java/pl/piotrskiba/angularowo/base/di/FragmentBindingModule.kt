package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment
import pl.piotrskiba.angularowo.main.punishment.details.ui.PunishmentDetailsFragment
import pl.piotrskiba.angularowo.main.punishment.list.ui.PunishmentListFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListContainerFragment

@Module(includes = [ViewModelModule::class])
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainScreenFragment(): MainScreenFragment

    @ContributesAndroidInjector
    abstract fun bindPlayerListFragment(): PlayerListFragment

    @ContributesAndroidInjector
    abstract fun bindPlayerDetailsFragment(): PlayerDetailsFragment

    @ContributesAndroidInjector
    abstract fun bindPunishmentListFragment(): PunishmentListFragment

    @ContributesAndroidInjector
    abstract fun bindPunishmentDetailsFragment(): PunishmentDetailsFragment

    @ContributesAndroidInjector
    abstract fun bindReportListContainerFragment(): ReportListContainerFragment
}
