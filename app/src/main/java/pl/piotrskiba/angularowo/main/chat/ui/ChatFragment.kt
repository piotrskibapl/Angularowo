package pl.piotrskiba.angularowo.main.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentChatBinding
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.chat.model.ChatMessage
import pl.piotrskiba.angularowo.main.chat.nav.ChatNavigator
import pl.piotrskiba.angularowo.main.chat.viewmodel.ChatViewModel
import pl.piotrskiba.angularowo.main.player.list.ui.PlayerListFragmentDirections

class ChatFragment : BaseFragment<ChatViewModel>(ChatViewModel::class), ChatNavigator {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.chatMessagesBinding.bindExtra(BR.navigator, this)
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = setupBinding(layoutInflater, container)
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_chat)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onChatMessageClick(chatMessage: ChatMessage) {
        findNavController().navigate(
            PlayerListFragmentDirections.toPlayerDetailsFragment(mainViewModel.player.value!!, chatMessage.uuid, previewedPlayerBanner = null)
        )
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        val binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }

    private fun setupRecyclerView() {
        val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.message_list)
        val layoutManager = recyclerView.layoutManager!! as LinearLayoutManager
        recyclerView.adapter = object : BindingRecyclerViewAdapter<ChatMessage>() {

            override fun setItems(items: MutableList<ChatMessage>?) {
                val lastIndex = itemCount - 1
                val lastVisibleIndex = layoutManager.findLastVisibleItemPosition()
                val shouldScroll = lastIndex == lastVisibleIndex
                super.setItems(items)
                if (shouldScroll) {
                    recyclerView.scrollToPosition(itemCount - 1)
                }
            }
        }
    }
}
