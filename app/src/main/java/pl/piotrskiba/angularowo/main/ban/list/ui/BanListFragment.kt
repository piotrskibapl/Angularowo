package pl.piotrskiba.angularowo.main.ban.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentBanListBinding
import pl.piotrskiba.angularowo.main.ban.list.viewmodel.BanListViewModel

class BanListFragment : BaseFragment<BanListViewModel>(BanListViewModel::class) {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = bindViewModel(layoutInflater, container)
        val view = binding.root

        ButterKnife.bind(this, view)

        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_ban_list)
        return view
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBanListBinding {
        val binding: FragmentBanListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_ban_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }
}