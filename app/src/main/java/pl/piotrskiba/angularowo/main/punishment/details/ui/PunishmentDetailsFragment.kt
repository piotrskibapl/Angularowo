package pl.piotrskiba.angularowo.main.punishment.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
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
        val binding = bindViewModel(layoutInflater, container)
        val view = binding.root
        return view
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPunishmentDetailsBinding {
        val binding: FragmentPunishmentDetailsBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_punishment_details, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun loadArguments() {
        val previewedPunishment = requireArguments().getSerializable(Constants.EXTRA_PUNISHMENT) as DetailedPunishmentData
        viewModel.previewedPunishmentData.value = previewedPunishment
    }
}
