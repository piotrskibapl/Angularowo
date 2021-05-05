package pl.piotrskiba.angularowo.main.player.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerListBinding
import pl.piotrskiba.angularowo.main.player.list.viewmodel.PlayerListViewModel

class PlayerListFragment : BaseFragment<PlayerListViewModel>(PlayerListViewModel::class) {

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

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerListBinding {
        val binding: FragmentPlayerListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_player_list, container, false)
        binding.viewModel = viewModel
        return binding
    }
}