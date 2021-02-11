package pl.piotrskiba.angularowo.base.di

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject constructor(
    private val viewModels: MutableMap<Class<out ViewModel>, @JvmSuppressWildcards Provider<ViewModel>>
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        viewModels[modelClass]?.get() as T
}

inline fun <reified VM : ViewModel> ViewModelProvider.Factory.obtainViewModel(activity: FragmentActivity): VM {
    return ViewModelProvider(activity, this).get(VM::class.java)
}
