package pl.piotrskiba.angularowo.main.report.list.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import pl.piotrskiba.angularowo.main.report.list.ui.ReportListTabFragment

private const val NUM_TABS = 2

class ReportListViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = NUM_TABS

    override fun createFragment(position: Int): Fragment = ReportListTabFragment()
}
