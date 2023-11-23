package pl.piotrskiba.angularowo.base.biometric

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticationResult.FAILURE
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticationResult.NOT_AVAILABLE
import pl.piotrskiba.angularowo.base.biometric.BiometricAuthenticationResult.SUCCESS
import javax.inject.Inject

class BiometricAuthenticator @Inject constructor() {

    fun requestWeakAuthentication(fragment: Fragment, listener: (BiometricAuthenticationResult) -> Unit) {
        val authenticators = BIOMETRIC_WEAK or DEVICE_CREDENTIAL
        when (canAuthenticate(authenticators, fragment.requireContext())) {
            true -> displayAuthentication(authenticators, fragment, listener)
            false -> listener(NOT_AVAILABLE)
        }
    }

    private fun canAuthenticate(authenticators: Int, context: Context): Boolean {
        val biometricManager = BiometricManager.from(context)
        return when (biometricManager.canAuthenticate(authenticators)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }
    }

    private fun displayAuthentication(authenticators: Int, fragment: Fragment, listener: (BiometricAuthenticationResult) -> Unit) {
        val context = fragment.requireContext()
        val executor = ContextCompat.getMainExecutor(context)
        val prompt = BiometricPrompt(
            fragment,
            executor,
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener(SUCCESS)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    listener(FAILURE)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    listener(FAILURE)
                }
            },
        )
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.biometric_authentication_dialog_title))
            .setSubtitle(context.getString(R.string.biometric_authentication_dialog_subtitle))
            .setAllowedAuthenticators(authenticators)
            .build()
        prompt.authenticate(promptInfo)
    }
}
