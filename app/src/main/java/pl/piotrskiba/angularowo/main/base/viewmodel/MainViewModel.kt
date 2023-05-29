package pl.piotrskiba.angularowo.main.base.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.network.usecase.ObserveUnauthorizedResponsesUseCase
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.base.nav.MainNavigator
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val observeUnauthorizedResponsesUseCase: ObserveUnauthorizedResponsesUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val player = MutableLiveData<DetailedPlayerModel>() // TODO: player should be dropped from main view model
    lateinit var navigator: MainNavigator

    override fun onFirstCreate() {
        super.onFirstCreate()
        disposables.add(
            observeUnauthorizedResponsesUseCase.execute(BuildConfig.VERSION_CODE)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe {
                    navigator.navigateToLogin()
                }
        )
    }
}
