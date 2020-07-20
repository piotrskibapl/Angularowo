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
import pl.piotrskiba.angularowo.database.entity.Friend
import pl.piotrskiba.angularowo.interfaces.NetworkErrorListener
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.models.PlayerList

class PlayerListFragment : Fragment(), PlayerClickListener, NetworkErrorListener {

    private lateinit var mViewModel: AppViewModel
    private lateinit var mPlayerListAdapter: PlayerListAdapter
    private lateinit var mFavoritePlayerListAdapter: PlayerListAdapter
    private lateinit var mFriendsObserver: Observer<List<Friend>>

    @BindView(R.id.rv_players)
    lateinit var mPlayerList: RecyclerView

    @BindView(R.id.rv_players_favorite)
    lateinit var mFavoritePlayerList: RecyclerView

    @BindView(R.id.swiperefresh)
    lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    @BindView(R.id.tv_no_players)
    lateinit var mNoPlayersTextView: TextView

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_player_list, container, false)

        ButterKnife.bind(this, view)

        setupPlayerListRecyclerView()
        setupFavoritePlayerListRecyclerView()

        mSwipeRefreshLayout.isRefreshing = true
        seekForPlayerList()

        mSwipeRefreshLayout.setOnRefreshListener { refreshData() }

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_player_list)
        return view
    }

    private fun setupPlayerListRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager
        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = LinearLayoutManager(context)
        } else {
            layoutManager = GridLayoutManager(context, 2)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_small)
            mPlayerList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
            mFavoritePlayerList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        }

        mPlayerListAdapter = PlayerListAdapter(requireContext(), this, mViewModel)
        mPlayerList.adapter = mPlayerListAdapter
        mPlayerList.layoutManager = layoutManager
    }

    private fun setupFavoritePlayerListRecyclerView() {
        val layoutManager: RecyclerView.LayoutManager
        val displayMode = resources.configuration.orientation
        if (displayMode == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = LinearLayoutManager(context)
        } else {
            layoutManager = GridLayoutManager(context, 2)
            val spacingInPixels = resources.getDimensionPixelSize(R.dimen.margin_small)
            mPlayerList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
            mFavoritePlayerList.addItemDecoration(SpacesItemDecoration(spacingInPixels))
        }

        mFavoritePlayerListAdapter = PlayerListAdapter(requireContext(), this, mViewModel)
        mFavoritePlayerList.adapter = mFavoritePlayerListAdapter
        mFavoritePlayerList.layoutManager = layoutManager
    }

    private fun seekForPlayerList() {
        mViewModel.setNetworkErrorListener(this)
        mViewModel.getPlayerList().observe(viewLifecycleOwner, Observer { playerList: PlayerList? ->
            if (playerList != null) {
                mSwipeRefreshLayout.isRefreshing = false

                if (this::mFriendsObserver.isInitialized && mViewModel.allFriends.hasObservers()) {
                    mViewModel.allFriends.removeObserver(mFriendsObserver)
                }

                if (playerList.players.isEmpty()) {
                    showNoPlayersLayout()
                }
                else {
                    mFriendsObserver = Observer {friends ->
                        val newPlayerList = ArrayList<Player>()
                        newPlayerList.addAll(playerList.players)
                        val newFavoritePlayerList = ArrayList<Player>()

                        if (friends != null) {
                            playerList.players.forEach { player ->
                                if (friends.contains(Friend(player.uuid))) {
                                    newPlayerList.remove(player)
                                    newFavoritePlayerList.add(player)
                                }
                            }
                        }

                        mPlayerListAdapter.setPlayerList(PlayerList(newPlayerList))
                        mFavoritePlayerListAdapter.setPlayerList(PlayerList(newFavoritePlayerList))

                        showDefaultLayout(newPlayerList.isNotEmpty(), newFavoritePlayerList.isNotEmpty())
                    }

                    mViewModel.allFriends.observe(viewLifecycleOwner, mFriendsObserver)
                }
            }
        })
    }

    private fun refreshData() {
        mSwipeRefreshLayout.isRefreshing = true
        mViewModel.refreshPlayerList()
    }

    override fun onPlayerClick(view: View, clickedPlayer: Player) {
        val intent = Intent(context, PlayerDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PLAYER, clickedPlayer)
        if (activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, getString(R.string.player_banner_transition_name))
            startActivity(intent, options.toBundle())
        } else {
            startActivity(intent)
        }
    }

    private fun showDefaultLayout(hasPlayers: Boolean, hasFavoritePlayers: Boolean) {
        mSwipeRefreshLayout.isRefreshing = false
        mNoPlayersTextView.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE

        if (hasPlayers) {
            mPlayerList.visibility = View.VISIBLE
        }
        else {
            mPlayerList.visibility = View.GONE
        }

        if (hasFavoritePlayers) {
            mFavoritePlayerList.visibility = View.VISIBLE
        }
        else {
            mFavoritePlayerList.visibility = View.GONE
        }
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