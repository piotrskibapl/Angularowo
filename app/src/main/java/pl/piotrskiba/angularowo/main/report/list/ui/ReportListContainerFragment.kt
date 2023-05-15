package pl.piotrskiba.angularowo.main.report.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentReportListContainerBinding
import pl.piotrskiba.angularowo.main.report.list.adapter.ReportListViewPagerAdapter
import pl.piotrskiba.angularowo.main.report.list.viewmodel.ReportListContainerViewModel

class ReportListContainerFragment : BaseFragment<ReportListContainerViewModel>(ReportListContainerViewModel::class) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = setupBinding(inflater, container)
        setupViewPager(binding)
        (activity as AppCompatActivity?)?.supportActionBar?.setTitle(R.string.actionbar_title_report_list)
        return binding.root
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentReportListContainerBinding.inflate(layoutInflater, container, false)

    private fun setupViewPager(binding: FragmentReportListContainerBinding) {
        viewModel.othersReportsTabAvailable.observe(viewLifecycleOwner) { othersReportsTabAvailable ->
            val adapter = ReportListViewPagerAdapter(
                childFragmentManager,
                lifecycle,
                othersReportsTabAvailable
            )
            // TODO: use data binding instead
            binding.viewpager.isUserInputEnabled = othersReportsTabAvailable
            binding.tablayout.visibility = if (othersReportsTabAvailable) View.VISIBLE else View.GONE
            binding.viewpager.adapter = adapter
            TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
                tab.text = when (position) {
                    0 -> requireContext().getString(R.string.reports_own)
                    else -> requireContext().getString(R.string.reports_others)
                }
            }.attach()
        }
        binding.viewpager.isSaveEnabled = false
    }
}
