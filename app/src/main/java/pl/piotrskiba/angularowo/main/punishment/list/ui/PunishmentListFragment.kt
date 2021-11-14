package pl.piotrskiba.angularowo.main.punishment.list.ui

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
import pl.piotrskiba.angularowo.databinding.FragmentPunishmentListBinding
import pl.piotrskiba.angularowo.main.punishment.details.ui.PunishmentDetailsActivity
import pl.piotrskiba.angularowo.main.punishment.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.punishment.list.viewmodel.PunishmentListViewModel
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData

class PunishmentListFragment : BaseFragment<PunishmentListViewModel>(PunishmentListViewModel::class),
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
        actionbar?.setTitle(R.string.actionbar_title_punishment_list)
        return view
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPunishmentListBinding {
        val binding: FragmentPunishmentListBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_punishment_list, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    override fun onPunishmentClick(view: View, punishment: PunishmentBannerData) {
        val intent = Intent(context, PunishmentDetailsActivity::class.java)
        intent.putExtra(Constants.EXTRA_PUNISHMENT, viewModel.punishments.find { it.id == punishment.id })
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            view,
            getString(R.string.punishment_banner_transition_name)
        )
        startActivity(intent, options.toBundle())
    }
}
