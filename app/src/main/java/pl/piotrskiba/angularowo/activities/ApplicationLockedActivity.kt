package pl.piotrskiba.angularowo.activities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.activities.base.BaseActivity
import pl.piotrskiba.angularowo.utils.RankUtils.getRankFromPreferences

class ApplicationLockedActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.tv_title)
    lateinit var mTitleTextView: TextView

    @BindView(R.id.tv_body)
    lateinit var mBodyTextView: TextView

    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_application_locked)

        ButterKnife.bind(this)

        setSupportActionBar(mToolbar)

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default_values)

        var title = mFirebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_TITLE_KEY)
        var body = mFirebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_BODY_KEY)
        title = title.replace("\\n", "\n")
        body = body.replace("\\n", "\n")

        mTitleTextView.text = title
        mBodyTextView.text = body
    }

    override fun onBackPressed() {
        // allow admins only to close this activity
        val rank = getRankFromPreferences(this)
        if (rank != null && rank.staff) {
            super.onBackPressed()
        }
    }
}