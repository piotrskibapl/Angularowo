package pl.piotrskiba.angularowo.utils

import java.util.*

object GlideUtils {

    /**
     * A function to get the signature version number based on the desired cache lifetime
     *
     * @param lifetimeInDays lifetime of the cache in days
     * @return signature version number
     */

    @JvmStatic
    fun getSignatureVersionNumber(lifetimeInDays: Int): Int {
        val calendar = Calendar.getInstance()

        return calendar[Calendar.YEAR] * 1000 + calendar[Calendar.DAY_OF_YEAR] / lifetimeInDays
    }
}