package pl.piotrskiba.angularowo.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.RESET
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.SUBSCRIBE
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionAction.UNSUBSCRIBE
import pl.piotrskiba.angularowo.main.base.handler.FCMTopicSubscriptionHandler
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val fcmTopicSubscriptionHandler: FCMTopicSubscriptionHandler,
) : LifecycleViewModel() {

    lateinit var navigator: SettingsNavigator
    val eventsChecked = MutableLiveData(false)
    val privateMessagesChecked = MutableLiveData(false)
    val accountIncidentsChecked = MutableLiveData(false)
    val newReportsChecked = MutableLiveData(false)

    fun onCreate() {
        eventsChecked.value = preferencesRepository.subscribedToFirebaseEventsTopic ?: false
        privateMessagesChecked.value = preferencesRepository.subscribedToFirebasePrivateMessagesTopic ?: false
        accountIncidentsChecked.value = preferencesRepository.subscribedToFirebaseAccountIncidentsTopic ?: false
        newReportsChecked.value = preferencesRepository.subscribedToFirebaseNewReportsTopic ?: false
    }

    fun onEventsNotificationsClicked() {
        val newValue = !eventsChecked.value!!
        fcmTopicSubscriptionHandler.handleNewEventsTopicSubscription(newValue.toFCMAction())
        eventsChecked.value = newValue
    }

    fun onPrivateMessagesNotificationsClicked() {
        val newValue = !privateMessagesChecked.value!!
        fcmTopicSubscriptionHandler.handlePrivateMessagesTopicSubscription(newValue.toFCMAction())
        privateMessagesChecked.value = newValue
    }

    fun onAccountIncidentsNotificationsClicked() {
        val newValue = !accountIncidentsChecked.value!!
        fcmTopicSubscriptionHandler.handleAccountIncidentsTopicSubscription(newValue.toFCMAction())
        accountIncidentsChecked.value = newValue
    }

    fun onNewReportsNotificationsClicked() {
        val newValue = !newReportsChecked.value!!
        fcmTopicSubscriptionHandler.handleNewReportsTopicSubscription(newValue.toFCMAction())
        newReportsChecked.value = newValue
    }

    fun onLogoutClicked() {
        navigator.onLogoutClicked {
            fcmTopicSubscriptionHandler.handlePlayerUuidTopicSubscription(RESET)
            fcmTopicSubscriptionHandler.handlePlayerRankTopicSubscription(RESET)
            fcmTopicSubscriptionHandler.handleNewEventsTopicSubscription(RESET)
            fcmTopicSubscriptionHandler.handlePrivateMessagesTopicSubscription(RESET)
            fcmTopicSubscriptionHandler.handleAccountIncidentsTopicSubscription(RESET)
            fcmTopicSubscriptionHandler.handleNewReportsTopicSubscription(RESET)
            preferencesRepository.clearUserData()
        }
    }

    private fun Boolean.toFCMAction() = when (this) {
        true -> SUBSCRIBE
        false -> UNSUBSCRIBE
    }
}
