package pl.piotrskiba.angularowo.main.report.list.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.rx.SchedulersProvider
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.domain.base.preferences.usecase.CheckIfIsStaffUserUseCase
import javax.inject.Inject

class ReportListContainerViewModel @Inject constructor(
    private val checkIfIsStaffUserUseCase: CheckIfIsStaffUserUseCase,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    var othersReportsTabAvailable: MutableLiveData<Boolean> = MutableLiveData(false)

    override fun onFirstCreate() {
        super.onFirstCreate()
        disposables.add(
            checkIfIsStaffUserUseCase.execute()
                .subscribeOn(facade.io())
                .observeOn(facade.ui())
                .subscribe { isStaff ->
                    othersReportsTabAvailable.value = isStaff
                }
        )
    }
}
