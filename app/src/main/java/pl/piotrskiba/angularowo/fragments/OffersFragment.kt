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
import pl.piotrskiba.angularowo.fragments.base.BaseFragment
import pl.piotrskiba.angularowo.interfaces.AdOfferClickListener
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.interfaces.OfferClickListener
import pl.piotrskiba.angularowo.models.AdOffer
import pl.piotrskiba.angularowo.models.DetailedPlayer
import pl.piotrskiba.angularowo.models.Offer
import pl.piotrskiba.angularowo.models.OffersInfo
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OffersFragment : BaseFragment(), AdOfferClickListener, OfferClickListener, NetworkErrorListener {

    private lateinit var mViewModel: AppViewModel
    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private lateinit var mOffersInfo: OffersInfo
    private lateinit var mAdOffersAdapter: AdOffersAdapter
    private lateinit var mOffersAdapter: OffersAdapter
    private var mPlayer: DetailedPlayer? = null

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_offers, container, false)

        ButterKnife.bind(this, view)

        setupAdOffersRecyclerView()
        setupOffersRecyclerView()
        seekForPlayerUpdates()
        seekForOffersInfo()

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_free_ranks)

        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        return view
    }

    private fun setupAdOffersRecyclerView() {
        mAdOffersAdapter = AdOffersAdapter(requireContext(), this)
        mAdOffersRecyclerView.adapter = mAdOffersAdapter

        val adOffersLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.ad_offer_list_span_count))
        mAdOffersRecyclerView.layoutManager = adOffersLayoutManager

        mAdOffersRecyclerView.setHasFixedSize(true)
        mAdOffersRecyclerView.isNestedScrollingEnabled = false
    }

    private fun setupOffersRecyclerView() {
        mOffersAdapter = OffersAdapter(requireContext(), this)
        mOffersRecyclerView.adapter = mOffersAdapter

        val offersLayoutManager = GridLayoutManager(context, resources.getInteger(R.integer.offer_list_span_count))
        mOffersRecyclerView.layoutManager = offersLayoutManager

        mOffersRecyclerView.setHasFixedSize(true)
        mOffersRecyclerView.isNestedScrollingEnabled = false
    }

    override fun onAdOfferClick(view: View, clickedAdOffer: AdOffer) {
        if (context != null && !mSwipeRefreshLayout.isRefreshing && clickedAdOffer.timeleft == 0) {
            AnalyticsUtils().logAdOfferDialogOpen(
                    mPlayer?.uuid ?: "",
                    mPlayer?.username ?: "",
                    clickedAdOffer.id
            )

            AlertDialog.Builder(requireContext())
                    .setTitle(R.string.ad_question)
                    .setMessage(R.string.ad_question_description)
                    .setPositiveButton(R.string.button_yes) { _: DialogInterface, _: Int ->
                        AnalyticsUtils().logAdOfferProceed(
                                mPlayer?.uuid ?: "",
                                mPlayer?.username ?: "",
                                clickedAdOffer.id
                        )

                        loadRewardedVideoAd(clickedAdOffer.adId)
                    }
                    .setNegativeButton(R.string.button_no) { _: DialogInterface, _: Int ->
                        AnalyticsUtils().logAdOfferCancel(
                                mPlayer?.uuid ?: "",
                                mPlayer?.username ?: "",
                                clickedAdOffer.id
                        )
                    }
                    .setOnCancelListener {
                        AnalyticsUtils().logAdOfferCancel(
                                mPlayer?.uuid ?: "",
                                mPlayer?.username ?: "",
                                clickedAdOffer.id
                        )
                    }
                    .show()
        }
    }

    override fun onOfferClick(view: View, clickedOffer: Offer) {
        if (context != null && !mSwipeRefreshLayout.isRefreshing && clickedOffer.timeleft == 0 && clickedOffer.price <= mOffersInfo.points) {
            AnalyticsUtils().logOfferDialogOpen(
                    mPlayer?.uuid ?: "",
                    mPlayer?.username ?: "",
                    clickedOffer.id
            )

            AlertDialog.Builder(requireContext())
                    .setTitle(R.string.offer_question)
                    .setMessage(resources.getQuantityString(R.plurals.offer_question_description, clickedOffer.price, clickedOffer.title, clickedOffer.price))
                    .setPositiveButton(R.string.button_yes) { _: DialogInterface, _: Int ->
                        AnalyticsUtils().logOfferProceed(
                                mPlayer?.uuid ?: "",
                                mPlayer?.username ?: "",
                                clickedOffer.id
                        )

                        redeemOffer(clickedOffer)
                    }
                    .setNegativeButton(R.string.button_no) { _: DialogInterface, _: Int ->
                        AnalyticsUtils().logOfferCancel(
                                mPlayer?.uuid ?: "",
                                mPlayer?.username ?: "",
                                clickedOffer.id
                        )
                    }
                    .setOnCancelListener {
                        AnalyticsUtils().logOfferCancel(
                                mPlayer?.uuid ?: "",
                                mPlayer?.username ?: "",
                                clickedOffer.id
                        )
                    }
                    .show()
        }
    }

    private fun seekForPlayerUpdates() {
        mViewModel.getPlayer().observe(viewLifecycleOwner, { player: DetailedPlayer? ->
            mPlayer = player
        })
    }

    private fun seekForOffersInfo() {
        showLoadingIndicator()
        mViewModel.setNetworkErrorListener(this)
        mViewModel.getOffersInfo().observe(viewLifecycleOwner, { offersInfo: OffersInfo? ->
            if (offersInfo != null) {
                mOffersInfo = offersInfo
                populateUi()
            }
        })
    }

    fun refreshData() {
        if (isAdded) {
            mSwipeRefreshLayout.isRefreshing = true
            mViewModel.refreshOffersInfo()
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
        if (this::mSwipeRefreshLayout.isInitialized) {
            mSwipeRefreshLayout.isRefreshing = true
        }
    }

    fun hideLoadingIndicator() {
        if (this::mSwipeRefreshLayout.isInitialized) {
            mSwipeRefreshLayout.isRefreshing = false
        }
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