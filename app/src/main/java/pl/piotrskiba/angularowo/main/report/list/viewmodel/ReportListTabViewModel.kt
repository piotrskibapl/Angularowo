package pl.piotrskiba.angularowo.main.report.list.viewmodel

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.model.ViewModelState.Error
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loaded
import pl.piotrskiba.angularowo.base.model.ViewModelState.Loading
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.report.usecase.GetOwnedReportsUseCase
import pl.piotrskiba.angularowo.main.report.model.ReportBannerData
import pl.piotrskiba.angularowo.main.report.model.toReportBannerDataList
import javax.inject.Inject

class ReportListTabViewModel @Inject constructor(
    private val getOwnedReportsUseCase: GetOwnedReportsUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading)
    val reportBanners: MutableLiveData<List<ReportBannerData>> = MutableLiveData()
    val reportsBinding = ItemBinding.of<ReportBannerData>(BR.report, R.layout.report_list_item)
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        super.onFirstCreate()
        loadReportList()
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun onRefresh() {
        loadReportList()
    }

    private fun loadReportList() {
        state.value = Loading
        disposables.add(getOwnedReportsUseCase
            .execute(preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { reportModels ->
                    state.value = Loaded
                    reportBanners.value = reportModels.toReportBannerDataList()
                },
                { error ->
                    state.value = Error(error)
                }
            )
        )
    }
}
