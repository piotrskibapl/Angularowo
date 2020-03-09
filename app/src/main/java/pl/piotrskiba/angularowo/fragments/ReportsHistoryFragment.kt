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
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.SpacesItemDecoration
import pl.piotrskiba.angularowo.activities.ReportDetailsActivity
import pl.piotrskiba.angularowo.adapters.ReportListAdapter
import pl.piotrskiba.angularowo.interfaces.ReportClickListener
import pl.piotrskiba.angularowo.models.Report
import pl.piotrskiba.angularowo.models.ReportList
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportsHistoryFragment(private val admin: Boolean) : Fragment(), ReportClickListener {

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_reports_history, container, false)

        ButterKnife.bind(this, view)

        val adapter = ReportListAdapter(context!!, this)
        mReportList.adapter = adapter

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

        loadReportList(adapter)

        mSwipeRefreshLayout.setOnRefreshListener { loadReportList(adapter) }

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_report_list)

        return view
    }

    private fun loadReportList(adapter: ReportListAdapter) {
        mSwipeRefreshLayout.isRefreshing = true

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val accessToken = sharedPreferences.getString(getString(R.string.pref_key_access_token), null)

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        if (admin) {
            serverAPIInterface.getAllReports(ServerAPIClient.API_KEY, false, accessToken!!).enqueue(object : Callback<ReportList?> {
                override fun onResponse(call: Call<ReportList?>, response: Response<ReportList?>) {
                    if (isAdded) {
                        if (response.isSuccessful && response.body() != null) {
                            if (response.body()!!.reportList.isNotEmpty()) {
                                adapter.setReportList(response.body()!!)
                                showDefaultLayout()
                            } else {
                                showNoReportsLayout()
                            }
                        } else if (!response.isSuccessful) {
                            showServerErrorLayout()
                        }
                    }
                }

                override fun onFailure(call: Call<ReportList?>, t: Throwable) {
                    if (isAdded)
                        showNoInternetLayout()

                    t.printStackTrace()
                }
            })
        } else {
            serverAPIInterface.getUserReports(ServerAPIClient.API_KEY, accessToken!!).enqueue(object : Callback<ReportList?> {
                override fun onResponse(call: Call<ReportList?>, response: Response<ReportList?>) {
                    if (isAdded) {
                        if (response.isSuccessful && response.body() != null) {
                            if (response.body()!!.reportList.isNotEmpty()) {
                                adapter.setReportList(response.body()!!)
                                showDefaultLayout()
                            } else {
                                showNoReportsLayout()
                            }
                        } else if (!response.isSuccessful) {
                            showServerErrorLayout()
                        }
                    }
                }

                override fun onFailure(call: Call<ReportList?>, t: Throwable) {
                    if (isAdded)
                        showNoInternetLayout()

                    t.printStackTrace()
                }
            })
        }
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
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view, getString(R.string.report_banner_transition_name))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

}