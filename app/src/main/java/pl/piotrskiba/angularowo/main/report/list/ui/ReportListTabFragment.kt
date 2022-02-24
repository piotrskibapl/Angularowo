package pl.piotrskiba.angularowo.main.report.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentReportListTabBinding
import pl.piotrskiba.angularowo.main.report.list.viewmodel.ReportListTabViewModel

const val OTHERS_REPORTS_VARIANT_KEY = "others-reports-variant"

class ReportListTabFragment : BaseFragment<ReportListTabViewModel>(ReportListTabViewModel::class) {

    override fun onCreate(savedInstanceState: Bundle?) {
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
