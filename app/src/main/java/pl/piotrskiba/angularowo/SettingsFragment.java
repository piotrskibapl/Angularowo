package pl.piotrskiba.angularowo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

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
}
