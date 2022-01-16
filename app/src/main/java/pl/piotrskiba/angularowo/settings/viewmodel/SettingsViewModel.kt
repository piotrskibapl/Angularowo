package pl.piotrskiba.angularowo.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.UNSUBSCRIBE
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionHandler
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val fcmTopicSubscriptionHandler: FCMTopicSubscriptionHandler,
) : ViewModel() {

    lateinit var navigator: SettingsNavigator
    val eventsChecked = MutableLiveData(false)
    val privateMessagesChecked = MutableLiveData(false)
    val accountIncidentsChecked = MutableLiveData(false)
    val newReportsChecked = MutableLiveData(false)

    fun onEventsNotificationsClicked() {
        eventsChecked.apply { value = !value!! }
    }

    fun onPrivateMessagesNotificationsClicked() {
        privateMessagesChecked.apply { value = !value!! }
    }

    fun onAccountIncidentsNotificationsClicked() {
        accountIncidentsChecked.apply { value = !value!! }
    }

    fun onNewReportsNotificationsClicked() {
        newReportsChecked.apply { value = !value!! }
    }

    fun onLogoutClicked() {
        navigator.onLogoutClicked {
            fcmTopicSubscriptionHandler.handlePlayerUuidTopicSubscription(UNSUBSCRIBE)
            fcmTopicSubscriptionHandler.handlePlayerRankTopicSubscription(UNSUBSCRIBE)
            fcmTopicSubscriptionHandler.handleNewEventsTopicSubscription(UNSUBSCRIBE)
            fcmTopicSubscriptionHandler.handlePrivateMessagesTopicSubscription(UNSUBSCRIBE)
            fcmTopicSubscriptionHandler.handleAccountIncidentsTopicSubscription(UNSUBSCRIBE)
            preferencesRepository.clearUserData()
        }
    }
}
