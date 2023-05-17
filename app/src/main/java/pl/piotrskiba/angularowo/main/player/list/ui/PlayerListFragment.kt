package pl.piotrskiba.angularowo.main.player.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerListBinding
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsActivity
import pl.piotrskiba.angularowo.main.player.list.nav.PlayerListNavigator
import pl.piotrskiba.angularowo.main.player.list.viewmodel.PlayerListViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBanner

class PlayerListFragment : BaseFragment<PlayerListViewModel>(PlayerListViewModel::class),
    PlayerListNavigator {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.playersBinding.bindExtra(BR.navigator, this)
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = setupBinding(layoutInflater, container)
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_player_list)
        return binding.root
    }

    override fun onPlayerClick(view: View, player: PlayerBanner) {
        val intent = Intent(context, PlayerDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PLAYER, mainViewModel.player.value!!)
        intent.putExtra(Constants.EXTRA_PREVIEWED_PLAYER, player)
        intent.putExtra(Constants.EXTRA_UUID, player.uuid)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            view,
            getString(R.string.player_banner_transition_name)
        )
        startActivity(intent, options.toBundle())
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerListBinding {
        val binding = FragmentPlayerListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}
