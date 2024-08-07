package pl.piotrskiba.angularowo.base.viewmodel

import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class LifecycleViewModel : ViewModel() {

    val disposables = CompositeDisposable()

    open fun onFirstCreate() {}

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
