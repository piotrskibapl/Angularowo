package pl.piotrskiba.angularowo.base.ui

import androidx.appcompat.app.AppCompatActivity
import pl.piotrskiba.angularowo.utils.AnalyticsUtils

open class OldBaseActivity : AppCompatActivity() {

    // TODO: move viewModelFactory injections from all activities to the BaseActivity after architecture refactor

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