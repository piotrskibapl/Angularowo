package pl.piotrskiba.angularowo.main.report.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.main.report.list.viewmodel.ReportListContainerViewModel

class ReportListContainerFragment : BaseFragment<ReportListContainerViewModel>(ReportListContainerViewModel::class) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report_list_container_fragment, container, false)
    }
}
