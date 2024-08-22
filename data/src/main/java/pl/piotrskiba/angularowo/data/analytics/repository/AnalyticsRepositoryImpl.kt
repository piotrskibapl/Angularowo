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

        object Target {
            const val UUID = "target_uuid"
            const val NAME = "target_name"
        }

        object Error {
            const val MESSAGE = "error_message"
        }
    }

    object Event {
        const val LOGIN = "login_success"
        const val LOGIN_ERROR = "login_failed"
        const val FAVORITE = "action_favorite"
        const val UNFAVORITE = "action_unfavorite"
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

    override fun logFavorite(playerUuid: String, playerName: String, targetUuid: String, targetName: String): Completable =
        Completable.fromAction {
            val bundle = Bundle()
            bundle.putString(AnalyticsData.Key.Player.UUID, playerUuid)
            bundle.putString(AnalyticsData.Key.Player.NAME, playerName)
            bundle.putString(AnalyticsData.Key.Target.UUID, targetUuid)
            bundle.putString(AnalyticsData.Key.Target.NAME, targetName)
            firebaseAnalytics.logEvent(AnalyticsData.Event.FAVORITE, bundle)
        }

    override fun logUnfavorite(playerUuid: String, playerName: String, targetUuid: String, targetName: String): Completable =
        Completable.fromAction {
            val bundle = Bundle()
            bundle.putString(AnalyticsData.Key.Player.UUID, playerUuid)
            bundle.putString(AnalyticsData.Key.Player.NAME, playerName)
            bundle.putString(AnalyticsData.Key.Target.UUID, targetUuid)
            bundle.putString(AnalyticsData.Key.Target.NAME, targetName)
            firebaseAnalytics.logEvent(AnalyticsData.Event.UNFAVORITE, bundle)
        }
}
