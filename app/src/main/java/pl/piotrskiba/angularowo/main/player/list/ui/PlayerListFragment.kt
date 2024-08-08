package pl.piotrskiba.angularowo.main.player.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerListBinding
import pl.piotrskiba.angularowo.main.player.list.nav.PlayerListNavigator
import pl.piotrskiba.angularowo.main.player.list.viewmodel.PlayerListViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner

class PlayerListFragment :
    BaseFragment<PlayerListViewModel>(PlayerListViewModel::class),
    PlayerListNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.playersBinding.bindExtra(BR.viewModel, viewModel)
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setupBinding(layoutInflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    override fun navigateToPlayerDetails(view: View, player: PlayerBanner) {
        findNavController().navigate(
            directions = PlayerListFragmentDirections.toPlayerDetailsFragment(player.uuid, player),
            navigatorExtras = FragmentNavigatorExtras(view to player.uuid),
        )
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlayerListBinding {
        val binding = FragmentPlayerListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }
}
