package pl.piotrskiba.angularowo.fragments

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.SpacesItemDecoration
import pl.piotrskiba.angularowo.activities.ReportDetailsActivity
import pl.piotrskiba.angularowo.adapters.ReportListAdapter
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.interfaces.ReportClickListener
import pl.piotrskiba.angularowo.models.Report
import pl.piotrskiba.angularowo.models.ReportList

class ReportsHistoryFragment() : Fragment(), ReportClickListener, NetworkErrorListener {

    @BindView(R.id.rv_reports)
    lateinit var mReportList: RecyclerView

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    @BindView(R.id.tv_no_reports)
    lateinit var mNoReportsTextView: TextView

    private lateinit var mReportListAdapter: ReportListAdapter
    private var admin: Boolean = false

    companion object {
        private const val ARGUMENT_ADMIN = "admin"

        fun newInstance(admin: Boolean): ReportsHistoryFragment = ReportsHistoryFragment().apply {
            arguments = bundleOf(ARGUMENT_ADMIN to admin)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        admin = requireArguments().getBoolean(ARGUMENT_ADMIN)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports_history, container, false)

        ButterKnife.bind(this, view)

        mReportListAdapter = ReportListAdapter(requireContext(), this)
        mReportList.adapter = mReportListAdapter

        val layoutManager: RecyclerView.LayoutManager
        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = LinearLayoutManager(context)
        } else {
            layoutManager = GridLayoutManager(context, 2)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_small)
            mReportList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        }
        mReportList.layoutManager = layoutManager

        mReportList.setHasFixedSize(true)

        mSwipeRefreshLayout.isRefreshing = true
        seekForReportList()

        mSwipeRefreshLayout.setOnRefreshListener { refreshReportList() }

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_report_list)

        return view
    }

    private fun seekForReportList() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.setNetworkErrorListener(this)

        if(admin) {
            viewModel.getAllReports().observe(viewLifecycleOwner, Observer { reportList: ReportList? ->
                if (reportList != null) {
                    mSwipeRefreshLayout.isRefreshing = false
                    mReportListAdapter.setReportList(reportList)

                    if (reportList.reportList.isEmpty())
                        showNoReportsLayout()
                    else
                        showDefaultLayout()
                }
            })
        }
        else {
            viewModel.getUserReports().observe(viewLifecycleOwner, Observer { reportList: ReportList? ->
                if (reportList != null) {
                    mSwipeRefreshLayout.isRefreshing = false
                    mReportListAdapter.setReportList(reportList)

                    if (reportList.reportList.isEmpty())
                        showNoReportsLayout()
                    else
                        showDefaultLayout()
                }
            })
        }
    }

    private fun refreshReportList() {
        mSwipeRefreshLayout.isRefreshing = true

        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        if(admin)
            viewModel.refreshAllReports()
        else
            viewModel.refreshUserReports()
    }

    private fun showDefaultLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mReportList.visibility = View.VISIBLE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mNoReportsTextView.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mReportList.visibility = View.GONE
        mNoInternetLayout.visibility = View.VISIBLE
        mServerErrorLayout.visibility = View.GONE
        mNoReportsTextView.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mReportList.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.VISIBLE
        mNoReportsTextView.visibility = View.GONE
    }

    private fun showNoReportsLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mReportList.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mNoReportsTextView.visibility = View.VISIBLE
        if (admin) {
            mNoReportsTextView.text = getString(R.string.error_no_reports_admin)
        } else {
            mNoReportsTextView.text = getString(R.string.error_no_reports)
        }
    }

    override fun onReportClick(view: View, clickedReport: Report) {
        val intent = Intent(context, ReportDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_REPORT, clickedReport)

        if (activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, getString(R.string.report_banner_transition_name))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    override fun onNoInternet() {
        showNoInternetLayout()
    }

    override fun onServerError() {
        showServerErrorLayout()
    }
}