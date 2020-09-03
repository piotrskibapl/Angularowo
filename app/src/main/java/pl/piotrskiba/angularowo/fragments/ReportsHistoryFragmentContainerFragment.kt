package pl.piotrskiba.angularowo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.ViewPagerFragmentAdapter
import pl.piotrskiba.angularowo.fragments.base.BaseFragment

class ReportsHistoryFragmentContainerFragment : BaseFragment() {

    @BindView(R.id.tablayout)
    lateinit var mTabLayout: TabLayout

    @BindView(R.id.viewpager)
    lateinit var mViewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports_history_fragment_container, container, false)

        ButterKnife.bind(this, view)

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_report_list)

        val adapter = ViewPagerFragmentAdapter(childFragmentManager, lifecycle)
        adapter.addFragment(ReportsHistoryFragment.newInstance(false))
        adapter.addFragment(ReportsHistoryFragment.newInstance(true))
        mViewPager.adapter = adapter

        mViewPager.isUserInputEnabled = false
        mViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        TabLayoutMediator(mTabLayout, mViewPager, TabConfigurationStrategy { tab: TabLayout.Tab, position: Int ->
            if (position == 0)
                tab.setText(R.string.reports_own)
            else
                tab.setText(R.string.reports_others)
        }).attach()

        return view
    }
}