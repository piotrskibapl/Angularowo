package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R

object PreferenceUtils {

    /**
     * Function for getting the access token from the preferences
     *
     * @param context
     * @return an access token or null if not set
     */
    @JvmStatic
    fun getAccessToken(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(context.getString(R.string.pref_key_access_token), null)
    }

    /**
     * Function for getting the username from the preferences
     *
     * @param context
     * @return an username or null if not set
     */
    @JvmStatic
    fun getUsername(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(context.getString(R.string.pref_key_nickname), null)
    }

    /**
     * Function for getting the rank name of user
     *
     * @param context
     * @return a rank name or null if not set
     */
    @JvmStatic
    fun getRankName(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(context.getString(R.string.pref_key_rank), null)
    }
}