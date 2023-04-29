package pl.piotrskiba.angularowo.settings.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivitySettingsBinding
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import pl.piotrskiba.angularowo.settings.viewmodel.SettingsViewModel

class SettingsActivity : BaseActivity(), SettingsNavigator {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        viewModel.navigator = this
        viewModel.onCreate() // TODO: handle onFirstCreate for Activity viewmodels
        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.settings)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupBinding() {
        viewModel = viewModelFactory.obtainViewModel(this)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    override fun displayLogoutConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout_question)
            .setMessage(R.string.logout_question_description)
            .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int ->
                onConfirm.invoke()
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }

    override fun closeActivity() {
        finish()
    }
}
