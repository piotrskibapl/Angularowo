package pl.piotrskiba.angularowo.utils

import java.util.*

object GlideUtils {
    @JvmStatic
    fun getSignatureVersionNumber(lifetimeInDays: Int): Int {
        val calendar = Calendar.getInstance()

        return calendar[Calendar.YEAR] * 1000 + calendar[Calendar.DAY_OF_YEAR] / lifetimeInDays
    }
}