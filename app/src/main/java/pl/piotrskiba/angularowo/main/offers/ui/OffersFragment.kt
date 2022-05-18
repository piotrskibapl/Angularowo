package pl.piotrskiba.angularowo.main.offers.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentOffersBinding
import pl.piotrskiba.angularowo.main.offers.model.AdOffer
import pl.piotrskiba.angularowo.main.offers.model.Offer
import pl.piotrskiba.angularowo.main.offers.nav.OffersNavigator
import pl.piotrskiba.angularowo.main.offers.viewmodel.OffersViewModel

class OffersFragment : BaseFragment<OffersViewModel>(OffersViewModel::class), OffersNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = setupBinding(layoutInflater, container)
        setupActionBar()
        return binding.root
    }

    override fun onAdOfferClick(adOffer: AdOffer) {
        // TODO: handle ad offer click
    }

    override fun onOfferClick(offer: Offer) {
        // TODO: handle offer click
    }

    private fun setupBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentOffersBinding {
        val binding = FragmentOffersBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun setupActionBar() {
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.actionbar_title_free_ranks)
    }
}
