package pl.piotrskiba.angularowo.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.activities.PlayerDetailsActivity
import pl.piotrskiba.angularowo.adapters.ChatAdapter
import pl.piotrskiba.angularowo.fragments.base.BaseFragment
import pl.piotrskiba.angularowo.interfaces.ChatMessageClickListener
import pl.piotrskiba.angularowo.models.ChatMessage
import pl.piotrskiba.angularowo.models.ChatMessageList
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import pl.piotrskiba.angularowo.utils.PreferenceUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ChatFragment : BaseFragment(), ChatMessageClickListener {

    private lateinit var preferenceUtils: PreferenceUtils
    private lateinit var mChatAdapter: ChatAdapter
    private lateinit var mOkHttpClient: OkHttpClient
    private lateinit var mWebSocket: WebSocket
    private lateinit var mViewModel: AppViewModel

    @BindView(R.id.rv_chat)
    lateinit var mChat: RecyclerView

    @BindView(R.id.pb_loading)
    lateinit var mLoadingIndicator: ProgressBar

    @BindView(R.id.no_internet_layout)
    lateinit var mNoInternetLayout: LinearLayout

    @BindView(R.id.server_error_layout)
    lateinit var mServerErrorLayout: LinearLayout

    @BindView(R.id.account_banned_layout)
    lateinit var mAccountBannedLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceUtils = PreferenceUtils(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        ButterKnife.bind(this, view)

        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)

        mChatAdapter = ChatAdapter(requireContext(), this)
        mChat.adapter = mChatAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        mChat.layoutManager = layoutManager

        mChat.setHasFixedSize(true)

        preloadChatMessages()

        mOkHttpClient = OkHttpClient()
        startChatWebSocket()

        val actionbar = (activity as AppCompatActivity?)!!.supportActionBar
        actionbar!!.setTitle(R.string.actionbar_title_chat)

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mWebSocket.close(1000, null)
    }

    private fun preloadChatMessages() {
        val accessToken = preferenceUtils.accessToken

        val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
        serverAPIInterface.getLastChatMessages(ServerAPIClient.API_KEY, accessToken!!).enqueue(object : Callback<ChatMessageList?> {
            override fun onResponse(call: Call<ChatMessageList?>, response: Response<ChatMessageList?>) {
                if (isAdded) {
                    if (response.isSuccessful && response.body() != null) {
                        val messages: ArrayList<ChatMessage> = response.body()!!.chatMessageList
                        messages.reverse()

                        mChatAdapter.setMessageList(ChatMessageList(messages))
                        mChat.scrollToPosition(mChatAdapter.itemCount - 1)

                        showDefaultLayout()
                    } else if (!response.isSuccessful) {
                        if (response.code() == 403)
                            showAccountBannedLayout()
                        else
                            showServerErrorLayout()
                    }
                }
            }

            override fun onFailure(call: Call<ChatMessageList?>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun showDefaultLayout() {
        mChat.visibility = View.VISIBLE
        mLoadingIndicator.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        mChat.visibility = View.GONE
        mLoadingIndicator.visibility = View.GONE
        mNoInternetLayout.visibility = View.VISIBLE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        mChat.visibility = View.GONE
        mLoadingIndicator.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.VISIBLE
        mAccountBannedLayout.visibility = View.GONE
    }

    private fun showAccountBannedLayout() {
        mChat.visibility = View.GONE
        mLoadingIndicator.visibility = View.GONE
        mNoInternetLayout.visibility = View.GONE
        mServerErrorLayout.visibility = View.GONE
        mAccountBannedLayout.visibility = View.VISIBLE
    }

    private fun startChatWebSocket() {
        val accessToken = preferenceUtils.accessToken

        val request = Request.Builder()
                .url(Constants.CHAT_WEBSOCKET_URL)
                .addHeader(Constants.CHAT_WEBSOCKET_ACCESSTOKEN_HEADER, accessToken!!)
                .build()

        val listener = ChatWebSocketListener()
        mWebSocket = mOkHttpClient.newWebSocket(request, listener)

        mOkHttpClient.dispatcher.executorService.shutdown()
    }

    override fun onChatMessageClick(view: View, clickedChatMessage: ChatMessage) {
        val intent = Intent(context, PlayerDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PLAYER, mViewModel.getPlayer().value)
        intent.putExtra(Constants.EXTRA_PREVIEWED_PLAYER,
                Player(
                        clickedChatMessage.uuid,
                        clickedChatMessage.username,
                        clickedChatMessage.rank,
                        false
                )
        )
        startActivity(intent)
    }

    private inner class ChatWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {}

        override fun onMessage(webSocket: WebSocket, text: String) {
            val messageInfo = text.split(";").toTypedArray()
            val uuid = messageInfo[0]
            val username = messageInfo[1]
            val rank = messageInfo[2]
            val body = messageInfo[3]

            val chatMessage = ChatMessage(uuid, username, rank, body)
            activity!!.runOnUiThread {
                if (!mChat.canScrollVertically(1)) {
                    mChatAdapter.addMessage(chatMessage)
                    mChat.scrollToPosition(mChatAdapter.itemCount - 1)
                } else {
                    mChatAdapter.addMessage(chatMessage)
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {}

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            // 1000 is the normal closure status
            webSocket.close(1000, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {}

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            t.printStackTrace()
            if (isAdded) {
                activity!!.runOnUiThread { showNoInternetLayout() }
            }
        }
    }
}