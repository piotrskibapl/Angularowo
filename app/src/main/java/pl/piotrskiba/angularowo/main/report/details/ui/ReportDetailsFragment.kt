package pl.piotrskiba.angularowo.main.report.details.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.base.ui.compose.setThemedContent
import pl.piotrskiba.angularowo.databinding.FragmentReportDetailsBinding
import pl.piotrskiba.angularowo.main.report.details.model.toUi
import pl.piotrskiba.angularowo.main.report.details.viewmodel.ReportDetailsViewModel
import pl.piotrskiba.angularowo.main.report.model.toReportBannerData

class ReportDetailsFragment : BaseFragment<ReportDetailsViewModel>(ReportDetailsViewModel::class) {

    private val args: ReportDetailsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        enterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
        exitTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.fade)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ) = FragmentReportDetailsBinding.inflate(inflater, container, false)
        .apply {
            composeView.setThemedContent {
                ReportDetailsView(banner = args.report.toReportBannerData(), report = args.report.toUi())
            }
        }.root
}
