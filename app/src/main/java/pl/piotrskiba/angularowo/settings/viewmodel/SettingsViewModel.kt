package pl.piotrskiba.angularowo.settings.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import pl.piotrskiba.angularowo.utils.TextUtils
import javax.inject.Inject

class SettingsViewModel @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val preferencesRepository: PreferencesRepository,
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
            firebaseMessaging.unsubscribeFromTopic(Constants.FIREBASE_PLAYER_UUID_TOPIC_PREFIX + preferencesRepository.uuid)
            firebaseMessaging.unsubscribeFromTopic(
                Constants.FIREBASE_RANK_TOPIC_PREFIX + TextUtils.normalize(preferencesRepository.rankName)
            )
            preferencesRepository.clearUserData()
        }
    }
}
