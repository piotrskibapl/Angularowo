package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.applock.ui.AppLockFragment
import pl.piotrskiba.angularowo.init.ui.InitFragment
import pl.piotrskiba.angularowo.login.ui.LoginFragment
import pl.piotrskiba.angularowo.main.chat.ui.ChatFragment
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragment
import pl.piotrskiba.angularowo.main.offers.ui.OffersFragment
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsFragment
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragment
import pl.piotrskiba.angularowo.main.punishment.details.ui.PunishmentDetailsFragment
import pl.piotrskiba.angularowo.main.punishment.list.ui.PunishmentListFragment
import pl.piotrskiba.angularowo.main.report.details.ui.ReportDetailsFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListContainerFragment
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListTabFragment
import pl.piotrskiba.angularowo.settings.ui.SettingsFragment

@Module(includes = [ViewModelModule::class])
abstract class FragmentBindingModule {

    @ContributesAndroidInjector
    abstract fun bindInitFragment(): InitFragment

    @ContributesAndroidInjector
    abstract fun bindLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    abstract fun bindAppLockFragment(): AppLockFragment

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

    @ContributesAndroidInjector
    abstract fun bindReportListTabFragment(): ReportListTabFragment

    @ContributesAndroidInjector
    abstract fun bindReportDetailsFragment(): ReportDetailsFragment

    @ContributesAndroidInjector
    abstract fun bindOffersFragment(): OffersFragment

    @ContributesAndroidInjector
    abstract fun bindChatFragment(): ChatFragment

    @ContributesAndroidInjector
    abstract fun bindSettingsFragment(): SettingsFragment
}
