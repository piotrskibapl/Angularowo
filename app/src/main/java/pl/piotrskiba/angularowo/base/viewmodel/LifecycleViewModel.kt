package pl.piotrskiba.angularowo.base.viewmodel

import android.content.Intent
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable

open class LifecycleViewModel : ViewModel() {

    lateinit var intent: Intent
    val disposables = CompositeDisposable()

    open fun onFirstCreate() {}

    override fun onCleared() {
        super.onCleared()
        disposables.clear()
    }
}
