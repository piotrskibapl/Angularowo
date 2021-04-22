package pl.piotrskiba.angularowo.base.viewmodel

import androidx.lifecycle.ViewModel

abstract class LifecycleViewModel: ViewModel() {

    abstract fun onFirstCreate()
}