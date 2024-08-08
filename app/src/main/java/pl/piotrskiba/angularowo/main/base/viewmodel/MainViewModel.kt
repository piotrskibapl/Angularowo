package pl.piotrskiba.angularowo.main.base.viewmodel

import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.extensions.applyDefaultSchedulers
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.network.usecase.ObserveUnauthorizedResponsesUseCase
import pl.piotrskiba.angularowo.main.base.nav.MainNavigator
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val observeUnauthorizedResponsesUseCase: ObserveUnauthorizedResponsesUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    lateinit var navigator: MainNavigator

    override fun onFirstCreate() {
        super.onFirstCreate()
        disposables.add(
            observeUnauthorizedResponsesUseCase.execute(BuildConfig.VERSION_CODE)
                .applyDefaultSchedulers(facade)
                .subscribe {
                    navigator.navigateToLogin()
                },
        )
    }
}
