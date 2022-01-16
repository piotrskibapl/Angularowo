package pl.piotrskiba.angularowo.settings.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivitySettingsBinding
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import pl.piotrskiba.angularowo.settings.viewmodel.SettingsViewModel
import javax.inject.Inject

class SettingsActivity : BaseActivity(), SettingsNavigator {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: SettingsViewModel

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
        viewModel.onCreate() // TODO: handle onFirstCreate for Activity viewmodels
        ButterKnife.bind(this)
        setSupportActionBar(mToolbar)

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

    private fun bindViewModel() {
        viewModel = viewModelFactory.obtainViewModel(this)
        val binding: ActivitySettingsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.navigator = this
    }

    override fun onLogoutClicked(successCallback: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle(R.string.logout_question)
            .setMessage(R.string.logout_question_description)
            .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int ->
                successCallback.invoke()
                finish()
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }
}
