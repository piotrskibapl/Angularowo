package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.Permissions
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.models.DetailedPlayer

class ApplicationLockedActivity : BaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.tv_title)
    lateinit var mTitleTextView: TextView

    @BindView(R.id.tv_body)
    lateinit var mBodyTextView: TextView

    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var mViewModel: AppViewModel
    private var mPlayer: DetailedPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_application_locked)
        ButterKnife.bind(this)
        setSupportActionBar(mToolbar)

        mViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default_values)

        seekForPlayerUpdates()

        var title = mFirebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_TITLE_KEY)
        var body = mFirebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_BODY_KEY)
        title = title.replace("\\n", "\n")
        body = body.replace("\\n", "\n")

        mTitleTextView.text = title
        mBodyTextView.text = body
    }

    private fun seekForPlayerUpdates() {
        mViewModel.getPlayer().observe(this, { mPlayer = it })
    }

    override fun onBackPressed() {
        if (mPlayer != null && mPlayer!!.hasPermission(Permissions.IGNORE_APP_LOCK)) {
            super.onBackPressed()
        } else {
            finishAffinity()
        }
    }
}