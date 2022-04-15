package pl.piotrskiba.angularowo.main.report.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentReportListTabBinding
import pl.piotrskiba.angularowo.main.report.details.ui.ReportDetailsActivity
import pl.piotrskiba.angularowo.main.report.list.nav.ReportListNavigator
import pl.piotrskiba.angularowo.main.report.list.viewmodel.ReportListTabViewModel
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData

const val OTHERS_REPORTS_VARIANT_KEY = "others-reports-variant"

class ReportListTabFragment : BaseFragment<ReportListTabViewModel>(ReportListTabViewModel::class),
    ReportListNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        viewModel.othersReportsVariant = arguments?.getBoolean(OTHERS_REPORTS_VARIANT_KEY) == true
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = bindViewModel(layoutInflater, container)
        return binding.root
    }

    override fun onReportClick(view: View, report: ReportBannerData) {
        val intent = Intent(context, ReportDetailsActivity::class.java)
        intent.putExtra(
            Constants.EXTRA_REPORT,
            viewModel.reportModels.value!!.first { it.id == report.id }
        )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            view,
            getString(R.string.report_banner_transition_name)
        )
        startActivity(intent, options.toBundle())
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReportListTabBinding {
        val binding: FragmentReportListTabBinding =
            DataBindingUtil.inflate(
                layoutInflater,
                R.layout.fragment_report_list_tab,
                container,
                false
            )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}
