package pl.piotrskiba.angularowo.main.mainscreen.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentMainScreenBinding
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.mainscreen.nav.MainScreenNavigator
import pl.piotrskiba.angularowo.main.mainscreen.viewmodel.MainScreenViewModel
import pl.piotrskiba.angularowo.main.punishment.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData

class MainScreenFragment : BaseFragment<MainScreenViewModel>(MainScreenViewModel::class), MainScreenNavigator, PunishmentListNavigator {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.punishmentsBinding.bindExtra(BR.navigator, this)
        super.onCreate(savedInstanceState)
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        viewModel.player.observe(this) { mainViewModel.player.value = it }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return setupBinding(layoutInflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.app_name)
    }

    override fun onPunishmentClick(view: View, punishment: PunishmentBannerData) {
        val detailedPunishmentData = viewModel.punishments.first { it.id == punishment.id }
        findNavController().navigate(
            directions = MainScreenFragmentDirections.toPunishmentDetailsFragment(detailedPunishmentData),
            navigatorExtras = FragmentNavigatorExtras(view to punishment.id),
        )
    }

    override fun openBrowser(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        if (context != null && intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        }
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainScreenBinding {
        val binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.navigator = this
        return binding
    }
}
