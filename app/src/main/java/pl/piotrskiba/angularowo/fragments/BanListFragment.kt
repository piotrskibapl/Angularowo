package pl.piotrskiba.angularowo.fragments

import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
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
import pl.piotrskiba.angularowo.activities.BanDetailsActivity
import pl.piotrskiba.angularowo.adapters.BanListAdapter
import pl.piotrskiba.angularowo.adapters.BanListAdapter.BanViewHolder
import pl.piotrskiba.angularowo.interfaces.BanClickListener
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.models.Ban
import pl.piotrskiba.angularowo.models.BanList

class BanListFragment : Fragment(), BanClickListener, NetworkErrorListener {

    private var mBanListAdapter: BanListAdapter? = null

    @BindView(R.id.rv_bans)
    lateinit var mBanList: RecyclerView

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_ban_list, container, false)

        ButterKnife.bind(this, view)

        mBanListAdapter = BanListAdapter(requireContext(), this)

        val layoutManager: RecyclerView.LayoutManager

        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = LinearLayoutManager(context)
        } else {
            layoutManager = GridLayoutManager(context, 2)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_small)
            mBanList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        }

        mBanList.adapter = mBanListAdapter
        mBanList.layoutManager = layoutManager
        mBanList.setHasFixedSize(true)

        mSwipeRefreshLayout.isRefreshing = true
        seekForBanList()

        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_ban_list)

        return view
    }

    private fun seekForBanList() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        viewModel.setNetworkErrorListener(this)
        viewModel.getBanList().observe(viewLifecycleOwner, Observer { banList: BanList? ->
            if (banList != null) {
                mSwipeRefreshLayout.isRefreshing = false
                mBanListAdapter?.setBanList(banList)
                showDefaultLayout()
            }
        })
    }

    private fun refreshData() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        mSwipeRefreshLayout.isRefreshing = true
        viewModel.refreshBanList()
    }

    override fun onBanClick(view: View, clickedBan: Ban) {
        val banViewHolder = view.tag as BanViewHolder

        val intent = Intent(context, BanDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_BAN, clickedBan)

        if (banViewHolder.mPlayerAvatar.drawable != null) {
            val avatarBitmap = (banViewHolder.mPlayerAvatar.drawable as BitmapDrawable).bitmap
            intent.putExtra(Constants.EXTRA_BITMAP, avatarBitmap)
        }

        if (activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, getString(R.string.ban_banner_transition_name))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    private fun showDefaultLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mBanList.visibility = View.VISIBLE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mBanList.visibility = View.GONE
        mNoInternetLayout.visibility = View.VISIBLE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mBanList.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.VISIBLE
    }

    override fun onNoInternet() {
        showNoInternetLayout()
    }

    override fun onServerError() {
        showServerErrorLayout()
    }
}