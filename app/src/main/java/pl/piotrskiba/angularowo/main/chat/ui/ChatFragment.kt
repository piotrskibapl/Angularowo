package pl.piotrskiba.angularowo.main.chat.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentChatBinding
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.chat.model.ChatMessage
import pl.piotrskiba.angularowo.main.chat.nav.ChatNavigator
import pl.piotrskiba.angularowo.main.chat.viewmodel.ChatViewModel
import pl.piotrskiba.angularowo.main.player.details.ui.PlayerDetailsFragment

class ChatFragment : BaseFragment<ChatViewModel>(ChatViewModel::class), ChatNavigator {

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val binding = setupBinding(layoutInflater, container)
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_chat)
        return binding.root
    }

    override fun onChatMessageClick(chatMessage: ChatMessage) {
        val intent = Intent(context, PlayerDetailsFragment::class.java)
        intent.putExtra(Constants.EXTRA_PLAYER, mainViewModel.player.value!!)
        // TODO: pass previewed player
        startActivity(intent)
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentChatBinding {
        val binding = FragmentChatBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}
