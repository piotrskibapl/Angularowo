package pl.piotrskiba.angularowo.applock.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.runtime.getValue
import pl.piotrskiba.angularowo.applock.viewmodel.AppLockViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.base.ui.compose.setThemedContent
import pl.piotrskiba.angularowo.databinding.FragmentAppLockBinding

class AppLockFragment : BaseFragment<AppLockViewModel>(AppLockViewModel::class) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentAppLockBinding.inflate(inflater, container, false)
        .apply {
            val data by viewModel.appLockData
            val state by viewModel.state
            composeView.setThemedContent { AppLockView(data, state) }
        }
        .root
}
