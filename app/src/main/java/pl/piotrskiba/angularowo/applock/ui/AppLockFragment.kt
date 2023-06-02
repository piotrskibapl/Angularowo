package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.piotrskiba.angularowo.applock.viewmodel.AppLockViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentAppLockBinding

class AppLockFragment : BaseFragment<AppLockViewModel>(AppLockViewModel::class) {

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
    ): FragmentAppLockBinding {
        val binding = FragmentAppLockBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }
}
