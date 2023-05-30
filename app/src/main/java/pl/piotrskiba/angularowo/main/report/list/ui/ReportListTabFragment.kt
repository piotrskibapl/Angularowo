package pl.piotrskiba.angularowo.main.report.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentReportListTabBinding
import pl.piotrskiba.angularowo.main.report.list.nav.ReportListNavigator
import pl.piotrskiba.angularowo.main.report.list.viewmodel.ReportListTabViewModel
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData

const val OTHERS_REPORTS_VARIANT_KEY = "others-reports-variant"

class ReportListTabFragment : BaseFragment<ReportListTabViewModel>(ReportListTabViewModel::class),
    ReportListNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.reportsBinding.bindExtra(BR.navigator, this)
        viewModel.othersReportsVariant = arguments?.getBoolean(OTHERS_REPORTS_VARIANT_KEY) == true
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = setupBinding(layoutInflater, container)
        return binding.root
    }

    override fun onReportClick(view: View, report: ReportBannerData) {
        val reportModel = viewModel.reportModels.value!!.first { it.id == report.id }
        findNavController().navigate(
            directions = ReportListContainerFragmentDirections.toReportDetailsFragment(reportModel),
            navigatorExtras = FragmentNavigatorExtras(view to report.id.toString())
        )
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentReportListTabBinding {
        val binding = FragmentReportListTabBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }
}
