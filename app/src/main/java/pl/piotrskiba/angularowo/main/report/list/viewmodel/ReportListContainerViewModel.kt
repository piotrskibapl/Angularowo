package pl.piotrskiba.angularowo.main.report.list.viewmodel

import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.utils.RankUtils
import javax.inject.Inject

class ReportListContainerViewModel @Inject constructor(
    val preferencesRepository: PreferencesRepository,
) : LifecycleViewModel() {

    // TODO: to be moved to an use case
    val othersReportsTabAvailable = RankUtils.getRankFromName(preferencesRepository.rankName!!).staff
}
