package pl.piotrskiba.angularowo.base.viewmodel

import androidx.lifecycle.ViewModel

open class LifecycleViewModel : ViewModel() {

    open fun onFirstCreate() {}
}
