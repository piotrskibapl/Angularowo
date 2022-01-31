package pl.piotrskiba.angularowo.main.report.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.main.report.list.adapter.ReportListViewPagerAdapter
import pl.piotrskiba.angularowo.main.report.list.viewmodel.ReportListContainerViewModel

class ReportListContainerFragment : BaseFragment<ReportListContainerViewModel>(ReportListContainerViewModel::class) {

    @BindView(R.id.viewpager)
    lateinit var viewPager: ViewPager2

    @BindView(R.id.tablayout)
    lateinit var tabLayout: TabLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_report_list_container, container, false)
        ButterKnife.bind(this, view)
        setupViewPager()
        return view
    }

    private fun setupViewPager() {
        val adapter = ReportListViewPagerAdapter(childFragmentManager, lifecycle)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> requireContext().getString(R.string.reports_own)
                else -> requireContext().getString(R.string.reports_others)
            }
        }.attach()
    }
}
