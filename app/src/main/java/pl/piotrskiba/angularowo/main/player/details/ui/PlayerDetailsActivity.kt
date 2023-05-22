package pl.piotrskiba.angularowo.main.player.details.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityPlayerDetailsBinding
import pl.piotrskiba.angularowo.main.player.details.nav.PlayerDetailsNavigator
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView

private const val SHOWCASE_DELAY_MS = 500

class PlayerDetailsActivity : BaseActivity<PlayerDetailsViewModel>(PlayerDetailsViewModel::class), PlayerDetailsNavigator {

    private lateinit var binding: ActivityPlayerDetailsBinding
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupToolbar()
        setupMenu()
        viewModel.navigator = this
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            supportFinishAfterTransition()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

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

    override fun displayFavoriteShowcase() {
        MaterialShowcaseView.Builder(this)
            .setTarget(findViewById(R.id.nav_favorite))
            .setTitleText(R.string.showcase_favorite_title)
            .setContentText(R.string.showcase_favorite_description)
            .setDelay(SHOWCASE_DELAY_MS)
            .setDismissOnTouch(true)
            .setTargetTouchable(true)
            .show()
    }

    private fun setupBinding() {
        binding = ActivityPlayerDetailsBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.player_info)
    }

    private fun setupMenu() {
        viewModel.menuItemsVisibility.observe(this) { invalidateOptionsMenu() }
        addMenuProvider(object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                with(viewModel.menuItemsVisibility.value!!) {
                    menu.findItem(R.id.nav_favorite)?.isVisible = favorite
                    menu.findItem(R.id.nav_unfavorite)?.isVisible = unfavorite
                    menu.findItem(R.id.nav_mute)?.isVisible = mute
                    menu.findItem(R.id.nav_kick)?.isVisible = kick
                    menu.findItem(R.id.nav_warn)?.isVisible = warn
                    menu.findItem(R.id.nav_ban)?.isVisible = ban
                }
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.player_details, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem) = onMenuItemClicked(menuItem)
        }, this, Lifecycle.State.RESUMED)
    }

    private fun onMenuItemClicked(menuItem: MenuItem) =
        when (menuItem.itemId) {
            R.id.nav_favorite -> {
                viewModel.onFavoriteClick()
                true
            }
            R.id.nav_unfavorite -> {
                viewModel.onUnfavoriteClick()
                true
            }
            else -> false
        }
}
