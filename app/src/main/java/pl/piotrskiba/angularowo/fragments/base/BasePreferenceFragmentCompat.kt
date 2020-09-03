package pl.piotrskiba.angularowo.fragments.base

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import pl.piotrskiba.angularowo.utils.AnalyticsUtils

open class BasePreferenceFragmentCompat: PreferenceFragmentCompat() {

    override fun onResume() {
        super.onResume()

        if (context != null && this::class.qualifiedName != null && this::class.simpleName != null) {
            AnalyticsUtils().logScreenView(
                    this::class.qualifiedName!!,
                    this::class.simpleName!!
            )
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {}
}