package pl.piotrskiba.angularowo.main.mainscreen.ui

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
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
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.punishmentsBinding.bindExtra(BR.navigator, this)
        super.onCreate(savedInstanceState)
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        requestNotificationsPermission()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewModel.player.observe(viewLifecycleOwner) { mainViewModel.player.value = it }
        viewModel.uiData.observe(viewLifecycleOwner) { uiData ->
            val navHeader = requireActivity().findViewById<NavigationView>(R.id.nav_view).getHeaderView(0)
            navHeader.findViewById<TextView>(R.id.navheader_username).text = uiData.player.username
            navHeader.findViewById<TextView>(R.id.navheader_rank).text = uiData.player.rankName
        }
        return setupBinding(layoutInflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
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
        container: ViewGroup?,
    ): FragmentMainScreenBinding {
        val binding = FragmentMainScreenBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.navigator = this
        return binding
    }

    private fun requestNotificationsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_DENIED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
