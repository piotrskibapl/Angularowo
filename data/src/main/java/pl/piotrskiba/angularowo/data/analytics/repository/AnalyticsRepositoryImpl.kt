package pl.piotrskiba.angularowo.data.analytics.repository

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import io.reactivex.rxjava3.core.Completable
import pl.piotrskiba.angularowo.domain.analytics.repository.AnalyticsRepository

private sealed class AnalyticsData {

    sealed class Key {

        object Player {
            const val UUID = "player_uuid"
            const val NAME = "player_name"
        }

        object Error {
            const val MESSAGE = "error_message"
        }
    }

    object Event {
        const val LOGIN = "login_success"
        const val LOGIN_ERROR = "login_failed"
    }
}

class AnalyticsRepositoryImpl(
    private val firebaseAnalytics: FirebaseAnalytics,
) : AnalyticsRepository {

    override fun logLogin(playerUuid: String, playerName: String): Completable =
        Completable.fromCallable {
            val bundle = Bundle()
            bundle.putString(AnalyticsData.Key.Player.UUID, playerUuid)
            bundle.putString(AnalyticsData.Key.Player.NAME, playerName)
            firebaseAnalytics.logEvent(AnalyticsData.Event.LOGIN, bundle)
        }

    override fun logLoginError(message: String?): Completable =
        Completable.fromCallable {
            val bundle = Bundle()
            bundle.putString(AnalyticsData.Key.Error.MESSAGE, message)
            firebaseAnalytics.logEvent(AnalyticsData.Event.LOGIN_ERROR, bundle)
        }
}
