package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import pl.piotrskiba.angularowo.applock.viewmodel.AppLockViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityAppLockBinding

class AppLockActivity : BaseActivity<AppLockViewModel>(AppLockViewModel::class) {

    private lateinit var binding: ActivityAppLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setSupportActionBar(binding.toolbar)
    }

    private fun setupBinding() {
        binding = ActivityAppLockBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    override fun onBackPressed() {
        if (viewModel.canSkip) {
            super.onBackPressed()
        } else {
            finishAffinity()
        }
    }
}
