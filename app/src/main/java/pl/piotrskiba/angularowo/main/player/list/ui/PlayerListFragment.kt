package pl.piotrskiba.angularowo.main.player.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerListBinding
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsActivity
import pl.piotrskiba.angularowo.main.player.list.nav.PlayerListNavigator
import pl.piotrskiba.angularowo.main.player.list.viewmodel.PlayerListViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData

class PlayerListFragment : BaseFragment<PlayerListViewModel>(PlayerListViewModel::class),
    PlayerListNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.navigator = this
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = bindViewModel(layoutInflater, container)
        val view = binding.root

        ButterKnife.bind(this, view)

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_player_list)
        return view
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigator = null
    }

    override fun onPlayerClick(player: PlayerBannerData) {
        // TODO: pass additional data
        val intent = Intent(context, PlayerDetailsActivity::class.java)
        when (activity) {
            null -> startActivity(intent)
            else -> {
                val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    requireActivity(),
                    requireView(),
                    getString(R.string.player_banner_transition_name)
                )
                startActivity(intent, options.toBundle())
            }
        }
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerListBinding {
        val binding: FragmentPlayerListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_player_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}
