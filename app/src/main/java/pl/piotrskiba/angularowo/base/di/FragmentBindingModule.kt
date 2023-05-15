package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.main.chat.ui.ChatFragment
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.offers.ui.OffersFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment
import pl.piotrskiba.angularowo.main.punishment.list.ui.PunishmentListFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListContainerFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListTabFragment

@Module(includes = [ViewModelModule::class])
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindMainScreenFragment(): MainScreenFragment

    @ContributesAndroidInjector
    abstract fun bindPlayerListFragment(): PlayerListFragment

    @ContributesAndroidInjector
    abstract fun bindPunishmentListFragment(): PunishmentListFragment

    @ContributesAndroidInjector
    abstract fun bindReportListContainerFragment(): ReportListContainerFragment

    @ContributesAndroidInjector
    abstract fun bindReportListTabFragment(): ReportListTabFragment

    @ContributesAndroidInjector
    abstract fun bindOffersFragment(): OffersFragment

    @ContributesAndroidInjector
    abstract fun bindChatFragment(): ChatFragment
}
