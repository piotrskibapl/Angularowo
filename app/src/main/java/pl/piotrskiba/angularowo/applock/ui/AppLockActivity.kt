package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Permissions
import pl.piotrskiba.angularowo.applock.viewmodel.AppLockViewModel
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityAppLockBinding

class AppLockActivity : BaseActivity() {

    private lateinit var viewModel: AppLockViewModel
    private lateinit var mainViewModel: AppViewModel
    private lateinit var binding: ActivityAppLockBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setSupportActionBar(binding.toolbar)
        viewModel.loadData() // TODO: onFirstCreate should be handled in viewmodel instead
        mainViewModel = ViewModelProvider(this)[AppViewModel::class.java]
    }

    private fun setupBinding() {
        viewModel = viewModelFactory.obtainViewModel(this)
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
