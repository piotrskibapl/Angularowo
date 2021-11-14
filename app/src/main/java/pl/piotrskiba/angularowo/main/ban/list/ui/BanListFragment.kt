package pl.piotrskiba.angularowo.main.ban.list.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentBanListBinding
import pl.piotrskiba.angularowo.main.ban.details.BanDetailsActivity
import pl.piotrskiba.angularowo.main.ban.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.ban.list.viewmodel.PunishmentListViewModel
import pl.piotrskiba.angularowo.main.ban.model.BanBannerData

class BanListFragment : BaseFragment<PunishmentListViewModel>(PunishmentListViewModel::class),
    PunishmentListNavigator {

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        super.onCreate(savedInstanceState)
    }

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

    override fun onPunishmentClick(view: View, punishment: BanBannerData) {
        val intent = Intent(context, BanDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_BAN, punishment)
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            view,
            getString(R.string.ban_banner_transition_name)
        )
        startActivity(intent, options.toBundle())
    }
}
