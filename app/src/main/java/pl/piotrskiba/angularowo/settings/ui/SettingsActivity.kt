package pl.piotrskiba.angularowo.settings.ui

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceManager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.messaging.FirebaseMessaging
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity

class SettingsActivity : BaseActivity(), OnSharedPreferenceChangeListener {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_settings)

        ButterKnife.bind(this)

        setSupportActionBar(mToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.settings)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        if (key == getString(R.string.pref_key_subscribed_to_new_reports)) {
            if (sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_new_reports), false)) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_REPORTS_TOPIC)
            }
        } else if (key == getString(R.string.pref_key_subscribed_to_private_messages)) {
            if (sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_private_messages), false)) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_PRIVATE_MESSAGES_TOPIC)
            }
        } else if (key == getString(R.string.pref_key_subscribed_to_account_incidents)) {
            if (sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_account_incidents), false)) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_ACCOUNT_INCIDENTS_TOPIC)
            }
        } else if (key == getString(R.string.pref_key_subscribed_to_events)) {
            if (sharedPreferences.getBoolean(getString(R.string.pref_key_subscribed_to_events), false)) {
                FirebaseMessaging.getInstance().subscribeToTopic(Constants.FIREBASE_NEW_EVENT_TOPIC)
            } else {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Constants.FIREBASE_NEW_EVENT_TOPIC)
            }
        }
    }
}