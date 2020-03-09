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
import pl.piotrskiba.angularowo.activities.PlayerDetailsActivity
import pl.piotrskiba.angularowo.adapters.PlayerListAdapter
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.models.PlayerList

class PlayerListFragment : Fragment(), PlayerClickListener, NetworkErrorListener {
    private lateinit var mPlayerListAdapter: PlayerListAdapter

    @BindView(R.id.rv_players)
    lateinit var mPlayerList: RecyclerView

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.tv_no_players)
    lateinit var mNoPlayersTextView: TextView

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_player_list, container, false)

        ButterKnife.bind(this, view)

        mPlayerListAdapter = PlayerListAdapter(context!!, this)
        mPlayerList.adapter = mPlayerListAdapter

        val layoutManager: RecyclerView.LayoutManager
        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = LinearLayoutManager(context)
        } else {
            layoutManager = GridLayoutManager(context, 2)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_small)
            mPlayerList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        }
        mPlayerList.layoutManager = layoutManager

        mPlayerList.setHasFixedSize(true)
        mSwipeRefreshLayout.isRefreshing = true
        seekForPlayerList()

        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_player_list)
        return view
    }

    private fun seekForPlayerList() {
        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.setNetworkErrorListener(this)
        viewModel.playerList.observe(viewLifecycleOwner, Observer { playerList: PlayerList? ->
            if (playerList != null) {
                mSwipeRefreshLayout.isRefreshing = false
                mPlayerListAdapter.setPlayerList(playerList)

                if (playerList.players.isEmpty())
                    showNoPlayersLayout()
                else
                    showDefaultLayout()
            }
        })
    }

    private fun refreshData() {
        mSwipeRefreshLayout.isRefreshing = true

        val viewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        viewModel.refreshPlayerList()
    }

    override fun onPlayerClick(view: View, clickedPlayer: Player) {
        val intent = Intent(context, PlayerDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PLAYER, clickedPlayer)
        if (activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!, view, getString(R.string.player_banner_transition_name))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    private fun showDefaultLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mNoPlayersTextView.visibility = View.GONE
        mPlayerList.visibility = View.VISIBLE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showNoPlayersLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mNoPlayersTextView.visibility = View.VISIBLE
        mPlayerList.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mNoPlayersTextView.visibility = View.GONE
        mPlayerList.visibility = View.GONE
        mNoInternetLayout.visibility = View.VISIBLE
        mServerErrorLayout.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        mSwipeRefreshLayout.isRefreshing = false
        mNoPlayersTextView.visibility = View.GONE
        mPlayerList.visibility = View.GONE
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