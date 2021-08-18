package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerDetailsBinding
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class PlayerDetailsFragment : BaseFragment<PlayerDetailsViewModel>(PlayerDetailsViewModel::class) {

    private lateinit var preferenceUtils: PreferenceUtils

    private lateinit var mViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(this).get(AppViewModel::class.java)
        preferenceUtils = PreferenceUtils(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = bindViewModel(layoutInflater, container)
        val view = binding.root

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.player_info)
        actionbar?.setDisplayHomeAsUpEnabled(true)
        actionbar?.setDisplayShowHomeEnabled(true)

        return view
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlayerDetailsBinding {
        val binding: FragmentPlayerDetailsBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_player_details, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO: handle punishments and favorite/unfavorite
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFinishAfterTransition()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}