package pl.piotrskiba.angularowo.main.punishment.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentPunishmentDetailsBinding
import pl.piotrskiba.angularowo.main.punishment.details.DetailedPunishmentData
import pl.piotrskiba.angularowo.main.punishment.details.viewmodel.PunishmentDetailsViewModel

class PunishmentDetailsFragment : BaseFragment<PunishmentDetailsViewModel>(PunishmentDetailsViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        loadArguments()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = setupBinding(layoutInflater, container)
        return binding.root
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPunishmentDetailsBinding {
        val binding = FragmentPunishmentDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun loadArguments() {
        val previewedPunishment = requireArguments().getSerializable(Constants.EXTRA_PUNISHMENT) as DetailedPunishmentData
        viewModel.previewedPunishmentData.value = previewedPunishment
    }
}
