package pl.piotrskiba.angularowo.main.offers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentOffersBinding
import pl.piotrskiba.angularowo.main.offers.viewmodel.OffersViewModel

class OffersFragment : BaseFragment<OffersViewModel>(OffersViewModel::class) {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = bindViewModel(layoutInflater, container)
        val view = binding.root
        ButterKnife.bind(this, view)
        setupActionBar()
        return view
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOffersBinding {
        val binding: FragmentOffersBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_offers, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun setupActionBar() {
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_free_ranks)
    }
}
