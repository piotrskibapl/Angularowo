package pl.piotrskiba.angularowo.base.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.piotrskiba.angularowo.applock.ui.AppLockActivity
import pl.piotrskiba.angularowo.login.ui.LoginActivity
import pl.piotrskiba.angularowo.main.base.ui.MainActivity
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsActivity
import pl.piotrskiba.angularowo.main.punishment.details.ui.PunishmentDetailsActivity
import pl.piotrskiba.angularowo.main.report.details.ui.ReportDetailsActivity
import pl.piotrskiba.angularowo.settings.ui.SettingsActivity

@Module(includes = [ViewModelModule::class])
abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun bindPlayerDetailsActivity(): PlayerDetailsActivity

    @ContributesAndroidInjector
    abstract fun bindPunishmentDetailsActivity(): PunishmentDetailsActivity

    @ContributesAndroidInjector
    abstract fun bindReportDetailsActivity(): ReportDetailsActivity

    @ContributesAndroidInjector
    abstract fun bindSettingsActivity(): SettingsActivity

    @ContributesAndroidInjector
    abstract fun bindAppLockActivity(): AppLockActivity
}
