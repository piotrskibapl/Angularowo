package pl.piotrskiba.angularowo.activities.base

import androidx.appcompat.app.AppCompatActivity
import pl.piotrskiba.angularowo.utils.AnalyticsUtils

open class BaseActivity : AppCompatActivity() {

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