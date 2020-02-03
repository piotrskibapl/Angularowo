package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.firebase.messaging.FirebaseMessaging;

import pl.piotrskiba.angularowo.models.Rank;
import pl.piotrskiba.angularowo.utils.RankUtils;
import pl.piotrskiba.angularowo.utils.TextUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String rankname = sharedPreferences.getString(getString(R.string.pref_key_rank), null);
        Rank rank = RankUtils.getRankFromName(rankname);
        if(rank != null && rank.isStaff()){
            addPreferencesFromResource(R.xml.pref_settings_admin);
        }
        else {
            addPreferencesFromResource(R.xml.pref_settings);
        }
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if(preference.getKey().equals(getString(R.string.pref_key_logout))){
            if(getContext() != null) {
                new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout_question)
                        .setMessage(R.string.logout_question_description)
                        .setPositiveButton(R.string.button_yes, (dialogInterface, i) -> {
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            if(sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_player_topic))) {
                                String username = sharedPreferences.getString(getString(R.string.pref_key_nickname), null);
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_PLAYER_TOPIC_PREFIX + username);
                                editor.remove(getString(R.string.pref_key_subscribed_to_player_topic));
                            }

                            String rank = sharedPreferences.getString(getString(R.string.pref_key_rank), null);
                            if(rank != null) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_RANK_TOPIC_PREFIX + TextUtils.normalize(rank));
                                editor.remove(getString(R.string.pref_key_rank));
                            }

                            if(sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_events))) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_EVENT_TOPIC);
                                editor.remove(getString(R.string.pref_key_subscribed_to_events));
                            }

                            if(sharedPreferences.contains(getString(R.string.pref_key_subscribed_to_new_reports))) {
                                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC);
                                editor.remove(getString(R.string.pref_key_subscribed_to_new_reports));
                            }


                            editor.remove(getString(R.string.pref_key_nickname));
                            editor.remove(getString(R.string.pref_key_access_token));

                            editor.commit();

                            if (getActivity() != null)
                                getActivity().finish();
                        })
                        .setNegativeButton(R.string.button_no, null)
                        .show();
            }
            return true;
        }

        return super.onPreferenceTreeClick(preference);
    }
}
