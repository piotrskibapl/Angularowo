package pl.piotrskiba.angularowo.main.report.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        return binding.root
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReportListContainerBinding {
        val binding = FragmentReportListContainerBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }

    private fun setupViewPager(binding: FragmentReportListContainerBinding) {
        viewModel.othersReportsTabAvailable.observe(viewLifecycleOwner) { othersReportsTabAvailable ->
            binding.viewpager.adapter = ReportListViewPagerAdapter(
                childFragmentManager,
                lifecycle,
                othersReportsTabAvailable
            )
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
