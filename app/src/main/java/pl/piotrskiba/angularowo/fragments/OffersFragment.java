package pl.piotrskiba.angularowo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.reward.RewardedVideoAd;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.AppViewModel;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.adapters.AdOffersAdapter;
import pl.piotrskiba.angularowo.adapters.OffersAdapter;
import pl.piotrskiba.angularowo.interfaces.AdOfferClickListener;
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener;
import pl.piotrskiba.angularowo.interfaces.OfferClickListener;
import pl.piotrskiba.angularowo.models.AdOffer;
import pl.piotrskiba.angularowo.models.Offer;
import pl.piotrskiba.angularowo.models.OffersInfo;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OffersFragment extends Fragment implements AdOfferClickListener, OfferClickListener, NetworkErrorListener {

    @BindView(R.id.swiperefresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.tv_coins)
    TextView mCoinsTextView;

    @BindView(R.id.rv_ad_offers)
    RecyclerView mAdOffersRecyclerView;

    @BindView(R.id.rv_offers)
    RecyclerView mOffersRecyclerView;

    @BindView(R.id.noRanksAvailableTextView)
    TextView mNoRanksAvailableTextView;

    @BindView(R.id.no_internet_layout)
    LinearLayout mNoInternetLayout;

    @BindView(R.id.server_error_layout)
    LinearLayout mServerErrorLayout;

    @BindView(R.id.account_banned_layout)
    LinearLayout mAccountBannedLayout;

    @BindView(R.id.default_layout)
    NestedScrollView mDefaultLayout;

    private RewardedVideoAd mRewardedVideoAd;

    private OffersInfo mOffersInfo;
    private AdOffersAdapter mAdOffersAdapter;
    private OffersAdapter mOffersAdapter;

    public OffersFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offers, container, false);

        ButterKnife.bind(this, view);

        setupAdOffersRecyclerView();
        setupOffersRecyclerView();

        seekForOffersInfo();

        ActionBar actionbar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionbar.setTitle(R.string.actionbar_title_free_ranks);

        mSwipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        return view;
    }

    private void setupAdOffersRecyclerView(){
        mAdOffersAdapter = new AdOffersAdapter(getContext(), this);

        RecyclerView.LayoutManager adOffersLayoutManager;
        adOffersLayoutManager = new GridLayoutManager(getContext(), 4);

        mAdOffersRecyclerView.setAdapter(mAdOffersAdapter);
        mAdOffersRecyclerView.setLayoutManager(adOffersLayoutManager);
        mAdOffersRecyclerView.setHasFixedSize(true);
        mAdOffersRecyclerView.setNestedScrollingEnabled(false);
    }

    private void setupOffersRecyclerView(){
        mOffersAdapter = new OffersAdapter(getContext(), this);

        RecyclerView.LayoutManager offersLayoutManager;
        offersLayoutManager = new GridLayoutManager(getContext(), 3);

        mOffersRecyclerView.setAdapter(mOffersAdapter);
        mOffersRecyclerView.setLayoutManager(offersLayoutManager);
        mOffersRecyclerView.setHasFixedSize(true);
        mOffersRecyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onAdOfferClick(View view, AdOffer clickedAdOffer) {
        if(getContext() != null && !mSwipeRefreshLayout.isRefreshing() && clickedAdOffer.getTimeleft() == 0) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.ad_question)
                    .setMessage(R.string.ad_question_description)
                    .setPositiveButton(R.string.button_yes, (dialogInterface, i) -> {
                        loadRewardedVideoAd(clickedAdOffer.getAdId());
                    })
                    .setNegativeButton(R.string.button_no, null)
                    .show();
        }
    }

    @Override
    public void onOfferClick(View view, Offer clickedOffer) {
        if(getContext() != null && !mSwipeRefreshLayout.isRefreshing() && clickedOffer.getTimeleft() == 0 && clickedOffer.getPrice() <= mOffersInfo.getPoints()) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.offer_question)
                    .setMessage(getResources().getQuantityString(R.plurals.offer_question_description, clickedOffer.getPrice(), clickedOffer.getTitle(), clickedOffer.getPrice()))
                    .setPositiveButton(R.string.button_yes, (dialogInterface, i) -> {
                        redeemOffer(clickedOffer);
                    })
                    .setNegativeButton(R.string.button_no, null)
                    .show();
        }
    }

    private void seekForOffersInfo(){
        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.setNetworkErrorListener(this);
        viewModel.getOffersInfo().observe(this, offersInfo -> {
            if(offersInfo != null){
                mOffersInfo = offersInfo;
                populateUi();
            }
        });
    }

    public void refreshData(){
        mSwipeRefreshLayout.setRefreshing(true);

        AppViewModel viewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);
        viewModel.refreshOffersInfo();
    }

    private void populateUi(){
        showDefaultLayout();

        mCoinsTextView.setText(String.valueOf(mOffersInfo.getPoints()));

        mAdOffersAdapter.setAdOfferList(mOffersInfo.getAdOffers());
        mOffersAdapter.setOfferList(mOffersInfo.getOffers(), mOffersInfo.getPoints());
    }

    private void loadRewardedVideoAd(String adId) {
        showLoadingIndicator();
        mRewardedVideoAd.loadAd(adId, new AdRequest.Builder().build());
    }

    private void redeemOffer(Offer offer){
        showLoadingIndicator();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String access_token = sharedPreferences.getString(getString(R.string.pref_key_access_token), null);

        Context context = getContext();

        ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
        serverAPIInterface.redeemOffer(ServerAPIClient.API_KEY, offer.getId(), access_token).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(isAdded()) {
                    new AlertDialog.Builder(context)
                            .setTitle(R.string.offer_redeemed)
                            .setMessage(R.string.offer_redeemed_description)

                            .setPositiveButton(R.string.button_dismiss, null)
                            .show();

                    refreshData();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showDefaultLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mDefaultLayout.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showNoRewardsLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.VISIBLE);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showNoInternetLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mServerErrorLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showServerErrorLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.VISIBLE);
        mAccountBannedLayout.setVisibility(View.GONE);
    }
    private void showAccountBannedLayout(){
        hideLoadingIndicator();
        mNoRanksAvailableTextView.setVisibility(View.GONE);
        mDefaultLayout.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.GONE);
        mServerErrorLayout.setVisibility(View.GONE);
        mAccountBannedLayout.setVisibility(View.VISIBLE);
    }

    private void showLoadingIndicator(){
        mSwipeRefreshLayout.setRefreshing(true);
    }
    public void hideLoadingIndicator(){
        mSwipeRefreshLayout.setRefreshing(false);
    }

    public void setRewardedVideoAd(RewardedVideoAd ad){
        mRewardedVideoAd = ad;
    }

    @Override
    public void onNoInternet() {
        showNoInternetLayout();
    }

    @Override
    public void onServerError() {
        showServerErrorLayout();
    }
}
