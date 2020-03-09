package pl.piotrskiba.angularowo.utils

import android.content.Context
import androidx.preference.PreferenceManager
import pl.piotrskiba.angularowo.R

object PreferenceUtils {
    @JvmStatic
    fun getAccessToken(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(context.getString(R.string.pref_key_access_token), null)
    }

    @JvmStatic
    fun getUsername(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(context.getString(R.string.pref_key_nickname), null)
    }

    @JvmStatic
    fun getRankName(context: Context): String? {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        return sharedPreferences.getString(context.getString(R.string.pref_key_rank), null)
    }
}