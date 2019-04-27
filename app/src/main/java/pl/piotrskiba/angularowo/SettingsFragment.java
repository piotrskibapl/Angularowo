package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AlertDialog;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import pl.piotrskiba.angularowo.utils.RankUtils;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String rank = sharedPreferences.getString(getString(R.string.pref_key_rank), null);
        if(rank != null && RankUtils.isStaffRank(rank)){
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
