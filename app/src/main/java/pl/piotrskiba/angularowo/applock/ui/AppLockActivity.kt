package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Permissions
import pl.piotrskiba.angularowo.applock.viewmodel.AppLockViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityAppLockBinding

class AppLockActivity : BaseActivity<AppLockViewModel>(AppLockViewModel::class) {

    private lateinit var mainViewModel: AppViewModel
    private lateinit var binding: ActivityAppLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setSupportActionBar(binding.toolbar)
        mainViewModel = ViewModelProvider(this)[AppViewModel::class.java]
    }

    private fun setupBinding() {
        binding = ActivityAppLockBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (mainViewModel.getPlayer().value != null && mainViewModel.getPlayer().value!!.hasPermission(Permissions.IGNORE_APP_LOCK)) {
            super.onBackPressed()
        } else {
            finishAffinity()
        }
    }
}
