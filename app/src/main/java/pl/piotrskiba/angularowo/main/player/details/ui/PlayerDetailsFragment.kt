package pl.piotrskiba.angularowo.main.player.details.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.EditText
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticationResult.FAILURE
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticationResult.NOT_AVAILABLE
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticationResult.SUCCESS
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticator
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPlayerDetailsBinding
import pl.piotrskiba.angularowo.layouts.TimeAmountPickerView
import pl.piotrskiba.angularowo.main.player.details.model.PunishmentType
import pl.piotrskiba.angularowo.main.player.details.nav.PlayerDetailsNavigator
import pl.piotrskiba.angularowo.main.player.details.viewmodel.PlayerDetailsViewModel
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val SHOWCASE_DELAY_MS = 500

class PlayerDetailsFragment : BaseFragment<PlayerDetailsViewModel>(PlayerDetailsViewModel::class), MenuProvider, PlayerDetailsNavigator {

    @Inject
    lateinit var biometricAuthenticator: BiometricAuthenticator
    private lateinit var binding: FragmentPlayerDetailsBinding
    private var snackbar: Snackbar? = null
    private var shouldDisplayFavoriteShowcase = false
    private val onGlobalLayoutListener = OnGlobalLayoutListener { displayFavoriteShowcaseIfReady() }
    private val args: PlayerDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.args = args
        super.onCreate(savedInstanceState)
        if (args.previewedPlayerBanner != null) {
            sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
            sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
            enterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
            exitTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = setupBinding(inflater, container)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onPause() {
        super.onPause()
        binding.root.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        viewModel.menuItemsVisibility.observe(viewLifecycleOwner) { requireActivity().invalidateOptionsMenu() }
    }

    override fun onPrepareMenu(menu: Menu) {
        if (viewModel.menuItemsVisibility.value != null) {
            with(viewModel.menuItemsVisibility.value!!) {
                menu.findItem(R.id.nav_favorite)?.isVisible = favorite
                menu.findItem(R.id.nav_unfavorite)?.isVisible = unfavorite
                menu.findItem(R.id.nav_mute)?.isVisible = mute
                menu.findItem(R.id.nav_kick)?.isVisible = kick
                menu.findItem(R.id.nav_warn)?.isVisible = warn
                menu.findItem(R.id.nav_ban)?.isVisible = ban
            }
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.player_details, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean =
        when (menuItem.itemId) {
            R.id.nav_favorite -> {
                viewModel.onFavoriteClick()
                true
            }

            R.id.nav_unfavorite -> {
                viewModel.onUnfavoriteClick()
                true
            }

            R.id.nav_mute -> {
                showPunishmentReasonDialog(
                    title = getString(R.string.player_details_mute_dialog_reason_title),
                    description = getString(R.string.player_details_mute_dialog_reason_description),
                ) { reason ->
                    showPunishmentTimeDialog(
                        title = getString(R.string.player_details_mute_dialog_time_title),
                        description = getString(R.string.player_details_mute_dialog_time_description),
                    ) { time ->
                        showPunishmentConfirmationDialog(
                            title = getString(R.string.player_details_mute_dialog_confirm_title),
                            description = getString(R.string.player_details_mute_dialog_confirm_description),
                        ) {
                            viewModel.onPunish(PunishmentType.MUTE, reason, time)
                        }
                    }
                }
                true
            }

            R.id.nav_kick -> {
                showPunishmentReasonDialog(
                    title = getString(R.string.player_details_kick_dialog_reason_title),
                    description = getString(R.string.player_details_kick_dialog_reason_description),
                ) { reason ->
                    showPunishmentConfirmationDialog(
                        title = getString(R.string.player_details_kick_dialog_confirm_title),
                        description = getString(R.string.player_details_kick_dialog_confirm_description),
                    ) {
                        viewModel.onPunish(PunishmentType.KICK, reason, time = null)
                    }
                }
                true
            }

            R.id.nav_warn -> {
                showPunishmentReasonDialog(
                    title = getString(R.string.player_details_warn_dialog_reason_title),
                    description = getString(R.string.player_details_warn_dialog_reason_description),
                ) { reason ->
                    showPunishmentConfirmationDialog(
                        title = getString(R.string.player_details_warn_dialog_confirm_title),
                        description = getString(R.string.player_details_warn_dialog_confirm_description),
                    ) {
                        viewModel.onPunish(PunishmentType.WARN, reason, TimeUnit.DAYS.toSeconds(3))
                    }
                }
                true
            }

            R.id.nav_ban -> {
                showPunishmentReasonDialog(
                    title = getString(R.string.player_details_ban_dialog_reason_title),
                    description = getString(R.string.player_details_ban_dialog_reason_description),
                ) { reason ->
                    showPunishmentTimeDialog(
                        title = getString(R.string.player_details_ban_dialog_time_title),
                        description = getString(R.string.player_details_ban_dialog_time_description),
                    ) { time ->
                        showPunishmentConfirmationDialog(
                            title = getString(R.string.player_details_ban_dialog_confirm_title),
                            description = getString(R.string.player_details_ban_dialog_confirm_description),
                        ) {
                            viewModel.onPunish(PunishmentType.BAN, reason, time)
                        }
                    }
                }
                true
            }

            else -> false
        }

    override fun displayMarkedAsFavoriteSnackbar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.player_details_marked_as_favorite),
            Snackbar.LENGTH_SHORT,
        )
        snackbar!!.show()
    }

    override fun displayUnmarkedAsFavoriteSnackbar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.player_details_unmarked_as_favorite),
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
        shouldDisplayFavoriteShowcase = true
        displayFavoriteShowcaseIfReady()
    }

    private fun displayFavoriteShowcaseIfReady() {
        val target = requireActivity().findViewById<View>(R.id.nav_favorite)
        if (shouldDisplayFavoriteShowcase && target != null) {
            shouldDisplayFavoriteShowcase = false
            MaterialShowcaseView.Builder(requireActivity())
                .setTarget(target)
                .setTitleText(R.string.player_details_showcase_favorite_title)
                .setContentText(R.string.player_details_showcase_favorite_description)
                .setDelay(SHOWCASE_DELAY_MS)
                .setDismissOnTouch(true)
                .setTargetTouchable(true)
                .show()
        }
    }

    override fun displayPunishmentSuccessDialog(type: PunishmentType) {
        showPunishmentSuccessDialog(
            getString(
                when (type) {
                    PunishmentType.MUTE -> R.string.player_details_mute_dialog_success_description
                    PunishmentType.KICK -> R.string.player_details_kick_dialog_success_description
                    PunishmentType.WARN -> R.string.player_details_warn_dialog_success_description
                    PunishmentType.BAN -> R.string.player_details_ban_dialog_success_description
                },
            ),
        )
    }

    override fun displayPunishmentErrorDialog() {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.dialog_error_title))
            .setMessage(getString(R.string.dialog_error_description))
            .setPositiveButton(R.string.button_dismiss, null)
            .show()
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPlayerDetailsBinding {
        val binding = FragmentPlayerDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        viewModel.navigator = this
        return binding
    }

    private fun showPunishmentReasonDialog(title: String, description: String, listener: (String) -> Unit) {
        val editText = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
        }
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(description)
            .setView(editText)
            .setPositiveButton(R.string.button_next) { _, _ ->
                val reason = editText.text.toString().trim()
                if (reason.isNotEmpty()) {
                    listener(reason)
                } else {
                    showPunishmentReasonDialog(title, description, listener)
                }
            }
            .show()
    }

    private fun showPunishmentTimeDialog(title: String, description: String, listener: (Long) -> Unit) {
        val timeAmountPicker = TimeAmountPickerView(requireContext()).apply {
            gravity = Gravity.CENTER_HORIZONTAL
        }
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(description)
            .setView(timeAmountPicker)
            .setPositiveButton(R.string.button_next) { _, _ ->
                val time = timeAmountPicker.getTimeAmount()
                if (time > 0) {
                    listener(time)
                } else {
                    showPunishmentTimeDialog(title, description, listener)
                }
            }
            .show()
    }

    private fun showPunishmentConfirmationDialog(title: String, description: String, listener: () -> Unit) {
        biometricAuthenticator.requestWeakAuthentication(this) { result ->
            when (result) {
                SUCCESS -> listener()
                FAILURE -> Unit
                NOT_AVAILABLE -> {
                    AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(description)
                        .setPositiveButton(R.string.button_yes) { _, _ ->
                            listener()
                        }
                        .setNegativeButton(R.string.button_cancel, null)
                        .show()
                }
            }
        }
    }

    private fun showPunishmentSuccessDialog(description: String) {
        AlertDialog.Builder(context)
            .setTitle(getString(R.string.player_details_punish_dialog_success_title))
            .setMessage(description)
            .setPositiveButton(R.string.button_dismiss, null)
            .show()
    }
}
