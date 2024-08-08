package pl.piotrskiba.angularowo.main.chat.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentChatBinding
import pl.piotrskiba.angularowo.main.chat.model.ChatMessage
import pl.piotrskiba.angularowo.main.chat.nav.ChatNavigator
import pl.piotrskiba.angularowo.main.chat.viewmodel.ChatViewModel

class ChatFragment : BaseFragment<ChatViewModel>(ChatViewModel::class), ChatNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.chatMessagesBinding.bindExtra(BR.viewModel, viewModel)
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return setupBinding(layoutInflater, container).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun navigateToPlayerDetails(previewedPlayerUuid: String) {
        findNavController().navigate(
            ChatFragmentDirections.toPlayerDetailsFragment(
                previewedPlayerUuid,
                previewedPlayerBanner = null,
            ),
        )
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?,
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
