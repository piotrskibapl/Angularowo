package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.adapters.ReportListAdapter;
import pl.piotrskiba.angularowo.interfaces.ReportClickListener;
import pl.piotrskiba.angularowo.models.Report;
import pl.piotrskiba.angularowo.models.ReportList;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportsHistoryFragment extends Fragment implements ReportClickListener {

    @BindView(R.id.rv_reports)
    RecyclerView mReportList;

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.tv_no_reports)
    TextView mNoReportsTextView;

    private boolean admin;

    public ReportsHistoryFragment(boolean admin){
        this.admin = admin;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reports_history, container, false);

        ButterKnife.bind(this, view);

        final ReportListAdapter adapter = new ReportListAdapter(getContext(), this);

        RecyclerView.LayoutManager layoutManager;
        int display_mode = getResources().getConfiguration().orientation;
        if (display_mode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new LinearLayoutManager(getContext());
        }
        else {
            layoutManager = new GridLayoutManager(getContext(), 2);

            int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.margin_small);
            mReportList.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        }

        mReportList.setAdapter(adapter);
        mReportList.setLayoutManager(layoutManager);
        mReportList.setHasFixedSize(true);

        loadReportList(adapter);

        mSwipeRefreshLayout.setOnRefreshListener(() -> loadReportList(adapter));

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_report_list);

        return view;
    }

    private void loadReportList(ReportListAdapter adapter){
        mSwipeRefreshLayout.setRefreshing(true);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        if(admin){
            serverAPIInterface.getAllReports(ServerAPIClient.API_KEY, false, access_token).enqueue(new Callback<ReportList>() {
                @Override
                public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                    if (isAdded()) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getReportList().size() > 0) {
                                adapter.setReportList(response.body());
                                showDefaultLayout();
                            } else {
                                showNoReportsLayout();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReportList> call, Throwable t) {
                    if (isAdded()) {
                        showNoInternetLayout();
                    }
                    t.printStackTrace();
                }
            });
        }
        else {
            serverAPIInterface.getUserReports(ServerAPIClient.API_KEY, access_token).enqueue(new Callback<ReportList>() {
                @Override
                public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                    if (isAdded()) {
                        if (response.isSuccessful() && response.body() != null) {
                            if (response.body().getReportList().size() > 0) {
                                adapter.setReportList(response.body());
                                showDefaultLayout();
                            } else {
                                showNoReportsLayout();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<ReportList> call, Throwable t) {
                    if (isAdded()) {
                        showNoInternetLayout();
                    }
                    t.printStackTrace();
                }
            });
        }
    }

    private void showDefaultLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mReportList.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mNoReportsTextView.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mReportList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mNoReportsTextView.setVisibility(View.GONE);
    }
    private void showNoReportsLayout(){
        mSwipeRefreshLayout.setRefreshing(false);
        mReportList.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mNoReportsTextView.setVisibility(View.VISIBLE);

        if(admin){
            mNoReportsTextView.setText(getString(R.string.error_no_reports_admin));
        }
        else{
            mNoReportsTextView.setText(getString(R.string.error_no_reports));
        }
    }

    @Override
    public void onReportClick(View view, Report clickedReport) {
        Intent intent = new Intent(getContext(), ReportDetailsActivity.class);
        intent.putExtra(Constants.EXTRA_REPORT, clickedReport);
        if(getActivity() != null) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(getActivity(), view, getString(R.string.report_banner_transition_name));
            startActivity(intent, options.toBundle());
        }
        else {
            startActivity(intent);
        }
    }
}
