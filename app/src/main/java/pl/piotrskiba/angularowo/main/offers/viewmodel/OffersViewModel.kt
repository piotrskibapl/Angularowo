package pl.piotrskiba.angularowo.main.offers.viewmodel

import androidx.lifecycle.MutableLiveData
import pl.piotrskiba.angularowo.base.model.ViewModelState
import pl.piotrskiba.angularowo.base.viewmodel.LifecycleViewModel
import javax.inject.Inject

class OffersViewModel @Inject constructor() : LifecycleViewModel() {

    val state = MutableLiveData<ViewModelState>(ViewModelState.Loading)
}
