package pl.piotrskiba.angularowo.utils

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

private const val PLAYER_UUID = "player_uuid"
private const val PLAYER_NAME = "player_name"
private const val TARGET_UUID = "target_uuid"
private const val TARGET_NAME = "target_name"

private const val EVENT_LOGIN = "login_success"
private const val EVENT_LOGIN_ERROR = "login_failed"
private const val EVENT_LOGOUT_START = "logout_open_dialog"
private const val EVENT_LOGOUT_PROCEED = "logout_logout"
private const val EVENT_LOGOUT_CANCEL = "logout_cancel"
private const val EVENT_FAVORITE = "action_favorite"
private const val EVENT_UNFAVORITE = "action_unfavorite"

class AnalyticsUtils {

    fun logLogin(playerUuid: String, playerName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        Firebase.analytics.logEvent(EVENT_LOGIN, bundle)
    }

    fun logLoginError(playerUuid: String, playerName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        Firebase.analytics.logEvent(EVENT_LOGIN_ERROR, bundle)
    }

    fun logLogoutStart(playerUuid: String, playerName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        Firebase.analytics.logEvent(EVENT_LOGOUT_START, bundle)
    }

    fun logLogoutProceed(playerUuid: String, playerName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        Firebase.analytics.logEvent(EVENT_LOGOUT_PROCEED, bundle)
    }

    fun logLogoutCancel(playerUuid: String, playerName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        Firebase.analytics.logEvent(EVENT_LOGOUT_CANCEL, bundle)
    }

    fun logScreenView(screenClass: String, screenName: String) {
        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, screenClass)
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, screenName)
        Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, bundle)
    }

    fun logFavorite(playerUuid: String, playerName: String, targetUuid: String, targetName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        bundle.putString(TARGET_UUID, targetUuid)
        bundle.putString(TARGET_NAME, targetName)
        Firebase.analytics.logEvent(EVENT_FAVORITE, bundle)
    }

    fun logUnfavorite(playerUuid: String, playerName: String, targetUuid: String, targetName: String) {
        val bundle = Bundle()
        bundle.putString(PLAYER_UUID, playerUuid)
        bundle.putString(PLAYER_NAME, playerName)
        bundle.putString(TARGET_UUID, targetUuid)
        bundle.putString(TARGET_NAME, targetName)
        Firebase.analytics.logEvent(EVENT_UNFAVORITE, bundle)
    }
}