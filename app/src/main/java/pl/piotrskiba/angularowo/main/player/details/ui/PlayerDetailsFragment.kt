package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.github.magneticflux.livedata.map
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerDetailsBinding
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayer
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class PlayerDetailsFragment : BaseFragment<PlayerDetailsViewModel>(PlayerDetailsViewModel::class) {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var preferenceUtils: PreferenceUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        preferenceUtils = PreferenceUtils(requireActivity())
        loadArguments()
        setupOptionsMenu()
        super.onCreate(savedInstanceState)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.player_details, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.nav_favorite)?.isVisible = !isPreviewingSelfOrPartner() && !isPreviewedPlayerFavorite()
        menu.findItem(R.id.nav_unfavorite)?.isVisible = !isPreviewingSelfOrPartner() && isPreviewedPlayerFavorite()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // TODO: handle punishments
        when (item.itemId) {
            android.R.id.home -> {
                requireActivity().supportFinishAfterTransition()
                return true
            }
            R.id.nav_favorite -> {
                viewModel.onFavoriteClick()
                return true
            }
            R.id.nav_unfavorite -> {
                viewModel.onUnfavoriteClick()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadArguments() {
        val player = requireArguments().getSerializable(Constants.EXTRA_PLAYER) as DetailedPlayer
        val previewedPlayer = requireArguments().getSerializable(Constants.EXTRA_PREVIEWED_PLAYER) as PlayerBannerData
        viewModel.player = player
        viewModel.previewedPlayerBanner.value = previewedPlayer
    }

    private fun setupOptionsMenu() {
        setHasOptionsMenu(true)
        viewModel.previewedPlayerBanner.observe(this, {
            requireActivity().invalidateOptionsMenu()
        })
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

    private fun isPreviewingSelfOrPartner() =
        viewModel.player.uuid == viewModel.previewedPlayerBanner.value?.uuid ||
            viewModel.player.partnerUuid == viewModel.previewedPlayerBanner.value?.uuid

    private fun isPreviewedPlayerFavorite() =
        viewModel.previewedPlayerBanner.value?.isFavorite == true
}