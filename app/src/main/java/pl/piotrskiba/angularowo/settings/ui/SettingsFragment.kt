package pl.piotrskiba.angularowo.settings.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentSettingsBinding
import pl.piotrskiba.angularowo.settings.nav.SettingsNavigator
import pl.piotrskiba.angularowo.settings.viewmodel.SettingsViewModel

class SettingsFragment : BaseFragment<SettingsViewModel>(SettingsViewModel::class), SettingsNavigator {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setupBinding(inflater, container).root
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
}
