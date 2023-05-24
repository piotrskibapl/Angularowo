package pl.piotrskiba.angularowo.main.punishment.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPunishmentListBinding
import pl.piotrskiba.angularowo.main.mainscreen.ui.MainScreenFragmentDirections
import pl.piotrskiba.angularowo.main.punishment.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.punishment.list.viewmodel.PunishmentListViewModel
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData

class PunishmentListFragment :
    BaseFragment<PunishmentListViewModel>(PunishmentListViewModel::class),
    PunishmentListNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.punishmentsBinding.bindExtra(BR.navigator, this)
        super.onCreate(savedInstanceState)
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

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPunishmentListBinding {
        val binding = FragmentPunishmentListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }

    override fun onPunishmentClick(view: View, punishment: PunishmentBannerData) {
        val detailedPunishmentData = viewModel.punishments.first { it.id == punishment.id }
        findNavController().navigate(
            directions = MainScreenFragmentDirections.toPunishmentDetailsFragment(detailedPunishmentData),
            navigatorExtras = FragmentNavigatorExtras(view to punishment.id),
        )
    }
}
