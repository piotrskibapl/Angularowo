package pl.piotrskiba.angularowo.main.report.list.viewmodel

import androidx.lifecycle.MutableLiveData
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.report.model.ReportModel
import pl.piotrskiba.angularowo.domain.report.usecase.GetNotArchivedReportsUseCase
import pl.piotrskiba.angularowo.domain.report.usecase.GetOwnedReportsUseCase
import pl.piotrskiba.angularowo.main.report.list.model.ReportListTabData
import pl.piotrskiba.angularowo.main.report.list.model.toUi
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData
import javax.inject.Inject

class ReportListTabViewModel @Inject constructor(
    private val getOwnedReportsUseCase: GetOwnedReportsUseCase,
    private val getNotArchivedReportsUseCase: GetNotArchivedReportsUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading.Fetch)
    val reportModels: MutableLiveData<List<ReportModel>> = MutableLiveData()
    val tabData: MutableLiveData<ReportListTabData> = MutableLiveData()
    val reportsBinding = ItemBinding.of<ReportBannerData>(BR.report, R.layout.report_list_item)
    var othersReportsVariant = false

    override fun onFirstCreate() {
        loadData()
    }

    fun onRefresh() {
        state.value = Loading.Refresh
        loadData()
    }

    private fun loadData() {
        if (othersReportsVariant) {
            loadOtherPlayersReportList()
        } else {
            loadOwnedReportList()
        }
    }

    private fun loadOwnedReportList() {
        disposables.add(getOwnedReportsUseCase
            .execute()
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { reportModels ->
                    state.value = Loaded
                    this.reportModels.value = reportModels
                    tabData.value = reportModels.toUi(othersReportsVariant)
                },
                { error ->
                    state.value = Error(error)
                }
            )
        )
    }

    private fun loadOtherPlayersReportList() {
        disposables.add(getNotArchivedReportsUseCase
            .execute()
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { reportModels ->
                    state.value = Loaded
                    this.reportModels.value = reportModels
                    tabData.value = reportModels.toUi(othersReportsVariant)
                },
                { error ->
                    state.value = Error(error)
                }
            )
        )
    }
}
