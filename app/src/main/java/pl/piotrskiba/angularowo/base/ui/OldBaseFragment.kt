package pl.piotrskiba.angularowo.base.ui

import androidx.fragment.app.Fragment
import pl.piotrskiba.angularowo.utils.AnalyticsUtils

open class OldBaseFragment: Fragment() {

    override fun onResume() {
        super.onResume()

        if (context != null && this::class.qualifiedName != null && this::class.simpleName != null) {
            AnalyticsUtils().logScreenView(
                this::class.qualifiedName!!,
                this::class.simpleName!!
            )
        }
    }
}