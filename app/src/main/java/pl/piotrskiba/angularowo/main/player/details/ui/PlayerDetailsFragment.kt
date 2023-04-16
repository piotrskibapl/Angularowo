package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerDetailsBinding
import pl.piotrskiba.angularowo.domain.player.model.DetailedPlayerModel
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.player.details.nav.PlayerDetailsNavigator
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import pl.piotrskiba.angularowo.main.player.model.PlayerBannerData
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class PlayerDetailsFragment : BaseFragment<PlayerDetailsViewModel>(PlayerDetailsViewModel::class),
    PlayerDetailsNavigator {

    private lateinit var mainViewModel: MainViewModel
    private lateinit var preferenceUtils: PreferenceUtils
    private lateinit var binding: FragmentPlayerDetailsBinding
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        preferenceUtils = PreferenceUtils(requireActivity())
        loadArguments()
        setupOptionsMenu()
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupBinding(layoutInflater, container)
        return binding.root
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
        val player = requireArguments().getSerializable(Constants.EXTRA_PLAYER) as DetailedPlayerModel
        val previewedPlayer = requireArguments().getSerializable(Constants.EXTRA_PREVIEWED_PLAYER) as PlayerBannerData
        viewModel.player = player
        viewModel.previewedPlayerBanner.value = previewedPlayer
    }

    private fun setupOptionsMenu() {
        setHasOptionsMenu(true)
        viewModel.previewedPlayerBanner.observe(this) {
            requireActivity().invalidateOptionsMenu()
        }
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) {
        binding = FragmentPlayerDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }

    private fun isPreviewingSelfOrPartner() =
        viewModel.player.uuid == viewModel.previewedPlayerBanner.value?.uuid ||
            viewModel.player.partnerUuid == viewModel.previewedPlayerBanner.value?.uuid

    private fun isPreviewedPlayerFavorite() =
        viewModel.previewedPlayerBanner.value?.isFavorite == true

    override fun displayMarkedAsFavoriteSnackbar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.marked_as_favorite),
            Snackbar.LENGTH_SHORT,
        )
        snackbar!!.show()
    }

    override fun displayUnmarkedAsFavoriteSnackbar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.unmarked_as_favorite),
            Snackbar.LENGTH_SHORT,
        )
        snackbar!!.show()
    }

    override fun displayGenericErrorSnackbar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.unknown_error),
            Snackbar.LENGTH_LONG,
        )
        snackbar!!.show()
    }
}
