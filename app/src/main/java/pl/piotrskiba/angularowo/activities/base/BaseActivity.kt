package pl.piotrskiba.angularowo.activities.base

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.piotrskiba.angularowo.utils.AnalyticsUtils
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onResume() {
        super.onResume()

        if (this::class.qualifiedName != null && this::class.simpleName != null) {
            AnalyticsUtils().logScreenView(
                this::class.qualifiedName!!,
                this::class.simpleName!!
            )
        }
    }
}