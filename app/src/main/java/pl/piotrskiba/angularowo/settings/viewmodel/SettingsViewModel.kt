package pl.piotrskiba.angularowo.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : ViewModel() {

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
    }
}
