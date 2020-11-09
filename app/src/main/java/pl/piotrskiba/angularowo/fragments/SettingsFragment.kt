package pl.piotrskiba.angularowo.fragments

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.preference.Preference
import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.fragments.base.BasePreferenceFragmentCompat
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromName
import pl.piotrskiba.angularowo.utils.TextUtils.normalize

class SettingsFragment : BasePreferenceFragmentCompat() {

    private lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceUtils = PreferenceUtils(requireContext())
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        val rankName = preferenceUtils.rankName
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
                val uuid = preferenceUtils.uuid
                val username = preferenceUtils.username

                AnalyticsUtils().logLogoutDialogOpen(
                        uuid ?: "",
                        username ?: ""
                )

                AlertDialog.Builder(requireContext())
                        .setTitle(R.string.logout_question)
                        .setMessage(R.string.logout_question_description)
                        .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int ->

                            AnalyticsUtils().logLogoutProceed(
                                    uuid ?: "",
                                    username ?: ""
                            )

                            FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + uuid)
                            FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + normalize(preferenceUtils.rankName))
                            preferenceUtils.clearUserData()

                            activity?.finish()
                        }
                        .setNegativeButton(R.string.button_no) { _: DialogInterface, _: Int ->
                            AnalyticsUtils().logLogoutCancel(
                                    uuid ?: "",
                                    username ?: ""
                            )
                        }
                        .setOnCancelListener {
                            AnalyticsUtils().logLogoutCancel(
                                    uuid ?: "",
                                    username ?: ""
                            )
                        }
                        .show()
            }
            return true
        }
        return super.onPreferenceTreeClick(preference)
    }
}