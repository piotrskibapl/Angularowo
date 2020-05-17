package pl.piotrskiba.angularowo.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromName
import pl.piotrskiba.angularowo.utils.TextUtils.normalize

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        val rankName = sharedPreferences.getString(getString(R.string.pref_key_rank), "")
        val rank = getRankFromName(rankName!!)

        if (rank.staff) {
            addPreferencesFromResource(R.xml.pref_settings_admin)
        } else {
            addPreferencesFromResource(R.xml.pref_settings)
        }
    }

    override fun onPreferenceTreeClick(preference: Preference): Boolean {
        if (preference.key == getString(R.string.pref_key_logout)) {
            if (context != null) {
                AlertDialog.Builder(requireContext())
                        .setTitle(R.string.logout_question)
                        .setMessage(R.string.logout_question_description)
                        .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int ->
                            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                            val editor = sharedPreferences.edit()

                            if (sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_player_topic))) {
                                val username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null)
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + username)
                                editor.remove(getString(R.string.pref_key_subscribed_to_player_topic))
                            }

                            val rank = sharedPreferences.getString(getString(R.string.pref_key_rank), null)
                            if (rank != null) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + normalize(rank))
                                editor.remove(getString(R.string.pref_key_rank))
                            }

                            if (sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_events))) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_EVENT_TOPIC)
                                editor.remove(getString(R.string.pref_key_subscribed_to_events))
                            }

                            if (sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports))) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
                                editor.remove(getString(R.string.pref_key_subscribed_to_new_reports))
                            }

                            editor.remove(getString(R.string.pref_key_nickname))
                            editor.remove(getString(R.string.pref_key_access_token))

                            editor.commit()
                            activity?.finish()
                        }
                        .setNegativeButton(R.string.button_no, null)
                        .show()
            }
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }
}