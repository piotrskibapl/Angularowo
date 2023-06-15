package pl.piotrskiba.angularowo.init.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.changeStartDestination
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.base.ui.compose.setThemedContent
import pl.piotrskiba.angularowo.databinding.FragmentInitBinding
import pl.piotrskiba.angularowo.init.nav.InitNavigator
import pl.piotrskiba.angularowo.init.viewmodel.InitViewModel

class InitFragment : BaseFragment<InitViewModel>(InitViewModel::class), InitNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentInitBinding.inflate(inflater, container, false)
        .apply {
            composeView.setThemedContent { InitView() }
        }.root

    override fun displayAppLock() {
        findNavController().navigate(
            InitFragmentDirections.toAppLockFragment(),
        )
    }

    override fun displayLogin() {
        findNavController().navigate(
            InitFragmentDirections.toLoginFragment(),
        )
    }

    override fun displayMainScreen() {
        findNavController().changeStartDestination(R.id.mainScreenFragment)
    }
}
