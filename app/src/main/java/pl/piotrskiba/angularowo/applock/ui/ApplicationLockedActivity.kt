package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.Permissions
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.OldBaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityApplicationLockedBinding
import pl.piotrskiba.angularowo.models.DetailedPlayer

class ApplicationLockedActivity : OldBaseActivity() {

    private lateinit var mFirebaseRemoteConfig: FirebaseRemoteConfig
    private lateinit var mViewModel: AppViewModel
    private lateinit var binding: ActivityApplicationLockedBinding
    private var mPlayer: DetailedPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setSupportActionBar(binding.toolbar)

        mViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        mFirebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_default_values)

        seekForPlayerUpdates()

        var title = mFirebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_TITLE_KEY)
        var body = mFirebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_BODY_KEY)
        title = title.replace("\\n", "\n")
        body = body.replace("\\n", "\n")

        binding.tvTitle.text = title
        binding.tvBody.text = body
    }

    private fun setupBinding() {
        binding = ActivityApplicationLockedBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun seekForPlayerUpdates() {
        mViewModel.getPlayer().observe(this) { mPlayer = it }
    }

    override fun onBackPressed() {
        if (mPlayer != null && mPlayer!!.hasPermission(Permissions.IGNORE_APP_LOCK)) {
            super.onBackPressed()
        } else {
            finishAffinity()
        }
    }
}
