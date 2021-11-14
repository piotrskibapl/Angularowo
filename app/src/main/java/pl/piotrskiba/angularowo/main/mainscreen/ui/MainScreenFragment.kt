package pl.piotrskiba.angularowo.main.mainscreen.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentMainScreenBinding
import pl.piotrskiba.angularowo.main.punishment.details.ui.PunishmentDetailsActivity
import pl.piotrskiba.angularowo.main.punishment.list.nav.PunishmentListNavigator
import pl.piotrskiba.angularowo.main.punishment.model.PunishmentBannerData
import pl.piotrskiba.angularowo.main.base.viewmodel.MainViewModel
import pl.piotrskiba.angularowo.main.mainscreen.viewmodel.MainScreenViewModel
import pl.piotrskiba.angularowo.models.Motd
import pl.piotrskiba.angularowo.utils.PreferenceUtils

class MainScreenFragment : BaseFragment<MainScreenViewModel>(MainScreenViewModel::class),
    PunishmentListNavigator {

    private lateinit var mViewModel: AppViewModel
    private lateinit var mainViewModel: MainViewModel
    private lateinit var preferenceUtils: PreferenceUtils

    private var motd: Motd? = null

    @BindView(R.id.tv_motd)
    lateinit var mMotdTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel.navigator = this
        super.onCreate(savedInstanceState)

        mViewModel = ViewModelProvider(requireActivity()).get(AppViewModel::class.java)
        mainViewModel = viewModelFactory.obtainViewModel(requireActivity())
        preferenceUtils = PreferenceUtils(requireContext())

        viewModel.player.observe(this, { mainViewModel.player.value = it })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = bindViewModel(layoutInflater, container)
        val view = binding.root

        ButterKnife.bind(this, view)

        mMotdTextView.setOnClickListener { onMotdClick() }

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val actionbar = (activity as AppCompatActivity?)?.supportActionBar
        actionbar?.setTitle(R.string.app_name)
    }

    private fun bindViewModel(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainScreenBinding {
        val binding: FragmentMainScreenBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.fragment_main_screen, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding
    }

    private fun onMotdClick() {
        if (motd?.url != null) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(motd!!.url))
            if (context != null && intent.resolveActivity(requireContext().packageManager) != null) {
                startActivity(intent)
            }
        }
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
