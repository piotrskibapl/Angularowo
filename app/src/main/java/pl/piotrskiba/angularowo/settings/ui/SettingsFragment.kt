package pl.piotrskiba.angularowo.settings.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentSettingsBinding
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import pl.piotrskiba.angularowo.settings.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment<SettingsViewModel>(SettingsViewModel::class), SettingsNavigator {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        if (granted) {
            viewModel.notificationsEnabled.value = true
        } else {
            if (!shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS)) {
                showNotificationsPermanentlyDeniedDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkNotificationsPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setupBinding(inflater, container).root
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun askForNotificationsPermission() {
        requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun displayLogoutConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.logout_question)
            .setMessage(R.string.logout_question_description)
            .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int ->
                onConfirm.invoke()
            }
            .setNegativeButton(R.string.button_no) { _, _ -> }
            .show()
    }

    override fun displayLogin() {
        findNavController().navigate(
            SettingsFragmentDirections.toLoginFragment()
        )
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSettingsBinding {
        val binding = FragmentSettingsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.navigator = this
        return binding
    }

    private fun checkNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            viewModel.notificationsEnabled.value = ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun showNotificationsPermanentlyDeniedDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(R.string.dialog_title_notifications_permission_denied_permanently)
            .setMessage(R.string.dialog_message_notifications_permission_denied_permanently)
            .setPositiveButton(R.string.dialog_proceed_button_notifications_permission_denied_permanently) { _: DialogInterface?, _: Int ->
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", requireContext().packageName, null)
                })
            }
            .setNegativeButton(android.R.string.cancel) { _, _ -> }
            .show()
    }
}
