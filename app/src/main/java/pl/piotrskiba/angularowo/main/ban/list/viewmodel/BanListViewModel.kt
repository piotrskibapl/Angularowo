package pl.piotrskiba.angularowo.main.ban.list.viewmodel

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.disposables.CompositeDisposable
import me.tatarka.bindingcollectionadapter2.ItemBinding
import pl.piotrskiba.angularowo.BR
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import pl.piotrskiba.angularowo.main.ban.model.BanBannerData
import javax.inject.Inject

class BanListViewModel @Inject constructor() : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(ViewModelState.Loading)
    val bans: ObservableList<BanBannerData> = ObservableArrayList()
    val bansBinding = ItemBinding.of<BanBannerData>(BR.ban, R.layout.ban_list_item)
    private val disposables = CompositeDisposable()

    override fun onFirstCreate() {
        super.onFirstCreate()
    }

    override fun onCleared() {
        disposables.clear()
    }

    fun onRefresh() {
    }
}
