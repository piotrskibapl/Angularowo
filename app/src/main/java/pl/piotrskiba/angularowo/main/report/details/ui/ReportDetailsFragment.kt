package pl.piotrskiba.angularowo.main.report.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentReportDetailsBinding
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.main.report.details.model.toUi
import pl.piotrskiba.angularowo.main.report.details.viewmodel.ReportDetailsViewModel
import pl.piotrskiba.angularowo.main.report.model.toReportBannerData

class ReportDetailsFragment : BaseFragment<ReportDetailsViewModel>(ReportDetailsViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadArguments()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = setupBinding(layoutInflater, container)
        return binding.root
    }

    private fun loadArguments() {
        val report = requireArguments().getSerializable(Constants.EXTRA_REPORT) as ReportModel
        viewModel.reportBanner = report.toReportBannerData()
        viewModel.reportDetails = report.toUi()
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReportDetailsBinding {
        val binding = FragmentReportDetailsBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}
