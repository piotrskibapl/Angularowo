package pl.piotrskiba.angularowo.base.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.piotrskiba.angularowo.login.viewmodel.LoginViewModel
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.mainscreen.viewmodel.MainScreenViewModel
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import pl.piotrskiba.angularowo.main.player.list.viewmodel.PlayerListViewModel
import pl.piotrskiba.angularowo.main.punishment.details.viewmodel.PunishmentDetailsViewModel
import pl.piotrskiba.angularowo.main.punishment.list.viewmodel.PunishmentListViewModel

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainScreenViewModel::class)
    abstract fun bindMainScreenViewModel(viewModel: MainScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerListViewModel::class)
    abstract fun bindPlayerListViewModel(viewModel: PlayerListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PlayerDetailsViewModel::class)
    abstract fun bindPlayerDetailsViewModel(viewModel: PlayerDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PunishmentListViewModel::class)
    abstract fun bindPunishmentListViewModel(viewModel: PunishmentListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PunishmentDetailsViewModel::class)
    abstract fun bindPunishmentDetailsViewModel(viewModel: PunishmentDetailsViewModel): ViewModel
}
