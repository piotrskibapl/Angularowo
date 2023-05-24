package pl.piotrskiba.angularowo.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.BuildConfig
import pl.piotrskiba.angularowo.base.rx.SchedulersFacade
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.settings.usecase.GetSettingsInitialValuesUseCase
import pl.piotrskiba.angularowo.domain.settings.usecase.LogoutUserUseCase
import pl.piotrskiba.angularowo.domain.settings.usecase.UpdateAccountIncidentsSubscriptionUseCase
import pl.piotrskiba.angularowo.domain.settings.usecase.UpdateNewEventsSubscriptionUseCase
import pl.piotrskiba.angularowo.domain.settings.usecase.UpdateNewReportsSubscriptionUseCase
import pl.piotrskiba.angularowo.domain.settings.usecase.UpdatePrivateMessagesSubscriptionUseCase
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val getSettingsInitialValuesUseCase: GetSettingsInitialValuesUseCase,
    private val updateNewEventsSubscriptionUseCase: UpdateNewEventsSubscriptionUseCase,
    private val updatePrivateMessagesSubscriptionUseCase: UpdatePrivateMessagesSubscriptionUseCase,
    private val updateAccountIncidentsSubscriptionUseCase: UpdateAccountIncidentsSubscriptionUseCase,
    private val updateNewReportsSubscriptionUseCase: UpdateNewReportsSubscriptionUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val facade: SchedulersFacade,
) : LifecycleViewModel() {

    lateinit var navigator: SettingsNavigator
    val eventsChecked = MutableLiveData(false)
    val privateMessagesChecked = MutableLiveData(false)
    val accountIncidentsChecked = MutableLiveData(false)
    val newReportsChecked = MutableLiveData(false)
    val newReportsVisible = MutableLiveData(false)

    override fun onFirstCreate() {
        disposables.add(
            getSettingsInitialValuesUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { settings ->
                    eventsChecked.value = settings.subscribedToEvents
                    privateMessagesChecked.value = settings.subscribedToPrivateMessages
                    accountIncidentsChecked.value = settings.subscribedToAccountIncidents
                    newReportsChecked.value = settings.subscribedToNewReports
                    newReportsVisible.value = settings.newReportsVisible
                }
        )
    }

    fun onEventsNotificationsClicked() {
        val newValue = !eventsChecked.value!!
        eventsChecked.value = newValue
        disposables.add(
            updateNewEventsSubscriptionUseCase.execute(newValue)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe()
        )
    }

    fun onPrivateMessagesNotificationsClicked() {
        val newValue = !privateMessagesChecked.value!!
        privateMessagesChecked.value = newValue
        disposables.add(
            updatePrivateMessagesSubscriptionUseCase.execute(newValue)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe()
        )
    }

    fun onAccountIncidentsNotificationsClicked() {
        val newValue = !accountIncidentsChecked.value!!
        accountIncidentsChecked.value = newValue
        disposables.add(
            updateAccountIncidentsSubscriptionUseCase.execute(newValue)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe()
        )
    }

    fun onNewReportsNotificationsClicked() {
        val newValue = !newReportsChecked.value!!
        newReportsChecked.value = newValue
        disposables.add(
            updateNewReportsSubscriptionUseCase.execute(newValue)
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe()
        )
    }

    fun onLogoutClicked() {
        navigator.displayLogoutConfirmationDialog {
            disposables.add(
                logoutUserUseCase.execute(BuildConfig.VERSION_CODE)
                    .subscribeOn(facade.io())
                    .observeOn(facade.ui())
                    .subscribe {
                        navigator.displayLogin()
                    }
            )
        }
    }
}
