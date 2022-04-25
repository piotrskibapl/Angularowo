package pl.piotrskiba.angularowo.main.chat.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.ChatAdapter
import pl.piotrskiba.angularowo.base.ui.OldBaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentChatBinding
import pl.piotrskiba.angularowo.interfaces.ChatMessageClickListener
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsFragment
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

class ChatFragment : OldBaseFragment(), ChatMessageClickListener {

    private lateinit var preferenceUtils: PreferenceUtils
    private lateinit var mChatAdapter: ChatAdapter
    private lateinit var mOkHttpClient: OkHttpClient
    private lateinit var mWebSocket: WebSocket
    private lateinit var mViewModel: AppViewModel
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceUtils = PreferenceUtils(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentChatBinding.inflate(layoutInflater, container, false)

        mViewModel = ViewModelProvider(requireActivity())[AppViewModel::class.java]

        mChatAdapter = ChatAdapter(requireContext(), this)
        binding.rvChat.adapter = mChatAdapter

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(context)
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.setHasFixedSize(true)

        preloadChatMessages()

        mOkHttpClient = OkHttpClient()
        startChatWebSocket()

        val actionbar = (activity as AppCompatActivity?)!!.supportActionBar
        actionbar!!.setTitle(R.string.actionbar_title_chat)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mWebSocket.close(1000, null)
        _binding = null
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
                        binding.rvChat.scrollToPosition(mChatAdapter.itemCount - 1)

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
        binding.rvChat.visibility = View.VISIBLE
        binding.pbLoading.visibility = View.GONE
        binding.noInternetLayout.main.visibility = View.GONE
        binding.serverErrorLayout.main.visibility = View.GONE
        binding.accountBannedLayout.main.visibility = View.GONE
    }

    private fun showNoInternetLayout() {
        binding.rvChat.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
        binding.noInternetLayout.main.visibility = View.VISIBLE
        binding.serverErrorLayout.main.visibility = View.GONE
        binding.accountBannedLayout.main.visibility = View.GONE
    }

    private fun showServerErrorLayout() {
        binding.rvChat.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
        binding.noInternetLayout.main.visibility = View.GONE
        binding.serverErrorLayout.main.visibility = View.VISIBLE
        binding.accountBannedLayout.main.visibility = View.GONE
    }

    private fun showAccountBannedLayout() {
        binding.rvChat.visibility = View.GONE
        binding.pbLoading.visibility = View.GONE
        binding.noInternetLayout.main.visibility = View.GONE
        binding.serverErrorLayout.main.visibility = View.GONE
        binding.accountBannedLayout.main.visibility = View.VISIBLE
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
        val intent = Intent(context, PlayerDetailsFragment::class.java)
        intent.putExtra(Constants.EXTRA_PLAYER, mViewModel.getPlayer().value)
        intent.putExtra(Constants.EXTRA_PREVIEWED_PLAYER,
                Player(
                        clickedChatMessage.uuid,
                        clickedChatMessage.uuid,
                        null,
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
                if (!binding.rvChat.canScrollVertically(1)) {
                    mChatAdapter.addMessage(chatMessage)
                    binding.rvChat.scrollToPosition(mChatAdapter.itemCount - 1)
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
