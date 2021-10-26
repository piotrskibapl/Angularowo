package pl.piotrskiba.angularowo.main.ban.list.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
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
import pl.piotrskiba.angularowo.domain.punishment.usecase.GetActivePunishmentsUseCase
import pl.piotrskiba.angularowo.main.ban.model.BanBannerData
import pl.piotrskiba.angularowo.main.ban.model.toUi
import javax.inject.Inject

class BanListViewModel @Inject constructor(
    private val getActivePunishmentsUseCase: GetActivePunishmentsUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val facade: SchedulersProvider,
) : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(Loading)
    val bans: ObservableList<BanBannerData> = ObservableArrayList()
    val bansBinding = ItemBinding.of<BanBannerData>(BR.ban, R.layout.ban_list_item)
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        loadBanList()
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun onRefresh() {
        loadBanList()
    }

    private fun loadBanList() {
        state.value = Loading
        disposables.add(getActivePunishmentsUseCase
            .execute(preferencesRepository.accessToken!!)
            .subscribeOn(facade.io())
            .observeOn(facade.ui())
            .subscribe(
                { punishmentModels ->
                    state.value = Loaded
                    bans.clear()
                    bans.addAll(punishmentModels.toUi())
                },
                { error ->
                    state.value = Error(error)
                    // TODO: provide error handling
                }
            )
        )
    }
}
