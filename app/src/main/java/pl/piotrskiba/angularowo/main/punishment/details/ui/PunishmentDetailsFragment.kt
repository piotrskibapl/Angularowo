package pl.piotrskiba.angularowo.main.punishment.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPunishmentDetailsBinding
import pl.piotrskiba.angularowo.main.punishment.details.viewmodel.PunishmentDetailsViewModel

class PunishmentDetailsFragment : BaseFragment<PunishmentDetailsViewModel>(PunishmentDetailsViewModel::class) {

    private val args: PunishmentDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        enterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        exitTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return setupBinding(inflater, container).root
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentPunishmentDetailsBinding {
        val binding = FragmentPunishmentDetailsBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.args = args
        return binding
    }
}
