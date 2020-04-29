package pl.piotrskiba.angularowo.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.reward.RewardedVideoAd
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.AdOffersAdapter
import pl.piotrskiba.angularowo.adapters.OffersAdapter
import pl.piotrskiba.angularowo.interfaces.AdOfferClickListener
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.interfaces.OfferClickListener
import pl.piotrskiba.angularowo.models.AdOffer
import pl.piotrskiba.angularowo.models.Offer
import pl.piotrskiba.angularowo.models.OffersInfo
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OffersFragment : Fragment(), AdOfferClickListener, OfferClickListener, NetworkErrorListener {
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private lateinit var mOffersInfo: OffersInfo
    private lateinit var mAdOffersAdapter: AdOffersAdapter
    private lateinit var mOffersAdapter: OffersAdapter

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.tv_coins)
    lateinit var mCoinsTextView: TextView

    @BindView(R.id.rv_ad_offers)
    lateinit var mAdOffersRecyclerView: RecyclerView

    @BindView(R.id.tv_limit_reached)
    lateinit var mOfferLimitReachedTextView: TextView

    @BindView(R.id.rv_offers)
    lateinit var mOffersRecyclerView: RecyclerView

    @BindView(R.id.noRanksAvailableTextView)
    lateinit var mNoRanksAvailableTextView: TextView

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    @BindView(R.id.account_banned_layout)
    lateinit var mAccountBannedLayout: LinearLayout

    @BindView(R.id.default_layout)
    lateinit var mDefaultLayout: NestedScrollView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)

        ButterKnife.bind(this, view)

        setupAdOffersRecyclerView()
        setupOffersRecyclerView()
        seekForOffersInfo()

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_free_ranks)

        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        return view
    }

    private fun setupAdOffersRecyclerView() {
        mAdOffersAdapter = AdOffersAdapter(context!!, this)
        mAdOffersRecyclerView.adapter = mAdOffersAdapter

        val adOffersLayoutManager = GridLayoutManager(context, 4)
        mAdOffersRecyclerView.layoutManager = adOffersLayoutManager

        mAdOffersRecyclerView.setHasFixedSize(true)
        mAdOffersRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupOffersRecyclerView() {
        mOffersAdapter = OffersAdapter(context!!, this)
        mOffersRecyclerView.adapter = mOffersAdapter

        val offersLayoutManager = GridLayoutManager(context, 3)
        mOffersRecyclerView.layoutManager = offersLayoutManager

        mOffersRecyclerView.setHasFixedSize(true)
        mOffersRecyclerView.isNestedScrollingEnabled = false
    }

    override fun onAdOfferClick(view: View, clickedAdOffer: AdOffer) {
        if (context != null && !mSwipeRefreshLayout.isRefreshing && clickedAdOffer.timeleft == 0) {
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.ad_question)
                    .setMessage(R.string.ad_question_description)
                    .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int -> loadRewardedVideoAd(clickedAdOffer.adId) }
                    .setNegativeButton(R.string.button_no, null)
                    .show()
        }
    }

    override fun onOfferClick(view: View, clickedOffer: Offer) {
        if (context != null && !mSwipeRefreshLayout.isRefreshing && clickedOffer.timeleft == 0 && clickedOffer.price <= mOffersInfo.points) {
            AlertDialog.Builder(context!!)
                    .setTitle(R.string.offer_question)
                    .setMessage(resources.getQuantityString(R.plurals.offer_question_description, clickedOffer.price, clickedOffer.title, clickedOffer.price))
                    .setPositiveButton(R.string.button_yes) { _: DialogInterface?, _: Int -> redeemOffer(clickedOffer) }
                    .setNegativeButton(R.string.button_no, null)
                    .show()
        }
    }

    private fun seekForOffersInfo() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.setNetworkErrorListener(this)
        viewModel.getOffersInfo().observe(viewLifecycleOwner, Observer { offersInfo: OffersInfo? ->
            if (offersInfo != null) {
                mOffersInfo = offersInfo
                populateUi()
            }
        })
    }

    fun refreshData() {
        if (isAdded) {
            mSwipeRefreshLayout.isRefreshing = true
            val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
            viewModel.refreshOffersInfo()
        }
    }

    private fun populateUi() {
        showDefaultLayout()
        mCoinsTextView.text = mOffersInfo.points.toString()
        mAdOffersAdapter.setAdOfferList(mOffersInfo.adOffers)
        mOffersAdapter.setOfferList(mOffersInfo.offers, mOffersInfo.points)

        var limitReached = true
        for (adOffer in mOffersInfo.adOffers) {
            if (adOffer.timeleft != -1) {
                limitReached = false
                break
            }
        }
        if (limitReached) {
            mOfferLimitReachedTextView.visibility = View.VISIBLE
        }
        else {
            mOfferLimitReachedTextView.visibility = View.GONE
        }
    }

    private fun loadRewardedVideoAd(adId: String) {
        showLoadingIndicator()
        mRewardedVideoAd.loadAd(adId, AdRequest.Builder().build())
    }

    private fun redeemOffer(offer: Offer) {
        showLoadingIndicator()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val accessToken = sharedPreferences.getString(getString(R.string.pref_key_access_token), null)
        val context = context

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.redeemOffer(ServerAPIClient.API_KEY, offer.id, accessToken!!).enqueue(object : Callback<Void?> {
            override fun onResponse(call: Call<Void?>, response: Response<Void?>) {
                if (isAdded) {
                    AlertDialog.Builder(context!!)
                            .setTitle(R.string.offer_redeemed)
                            .setMessage(R.string.offer_redeemed_description)
                            .setPositiveButton(R.string.button_dismiss, null)
                            .show()
                    refreshData()
                }
            }

            override fun onFailure(call: Call<Void?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun showDefaultLayout() {
        hideLoadingIndicator()
        mNoRanksAvailableTextView.visibility = View.GONE
        mDefaultLayout.visibility = View.VISIBLE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showNoRewardsLayout() {
        hideLoadingIndicator()
        mNoRanksAvailableTextView.visibility = View.VISIBLE
        mDefaultLayout.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        hideLoadingIndicator()
        mNoRanksAvailableTextView.visibility = View.GONE
        mDefaultLayout.visibility = View.GONE
        mNoInternetLayout.visibility = View.VISIBLE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        hideLoadingIndicator()
        mNoRanksAvailableTextView.visibility = View.GONE
        mDefaultLayout.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.VISIBLE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showAccountBannedLayout() {
        hideLoadingIndicator()
        mNoRanksAvailableTextView.visibility = View.GONE
        mDefaultLayout.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.VISIBLE
    }

    private fun showLoadingIndicator() {
        mSwipeRefreshLayout.isRefreshing = true
    }

    fun hideLoadingIndicator() {
        mSwipeRefreshLayout.isRefreshing = false
    }

    fun setRewardedVideoAd(ad: RewardedVideoAd) {
        mRewardedVideoAd = ad
    }

    override fun onNoInternet() {
        showNoInternetLayout()
    }

    override fun onServerError() {
        showServerErrorLayout()
    }
}