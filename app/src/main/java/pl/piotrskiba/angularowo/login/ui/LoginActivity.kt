package pl.piotrskiba.angularowo.login.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityLoginBinding
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.login.viewmodel.LoginViewModel

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var context: Context

    private var snackbar: Snackbar? = null
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setSupportActionBar(binding.toolbar)

        context = this

        binding.peetAccesstoken.setOnPinEnteredListener {
            viewModel.onPinEntered(it.toString())
            binding.peetAccesstoken.setText("")
            closeKeyboard()
            showLoadingSnackBar()
        }

        viewModel.loginState.observe(this) { loginState ->
            when (loginState) {
                is LoginState.Loading -> showLoadingSnackBar()
                is LoginState.Success -> onLoginSuccess()
                is LoginState.Error -> onLoginError(loginState.error)
                else -> snackbar?.dismiss()
            }
        }
    }

    private fun setupBinding() {
        viewModel = viewModelFactory.obtainViewModel(this)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setContentView(binding.root)
    }

    private fun onLoginSuccess() {
        setResult(Constants.RESULT_CODE_SUCCESS)
        finish()
    }

    private fun onLoginError(error: AccessTokenError) {
        val message = when (error) {
            is AccessTokenError.UnknownError -> getString(R.string.login_error_unknown)
            is AccessTokenError.CodeNotFoundError -> getString(R.string.login_error_code_not_found)
            is AccessTokenError.CodeExpiredError -> getString(R.string.login_error_code_expired)
        }
        showErrorSnackBar(message)
    }

    private fun showLoadingSnackBar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            getString(R.string.logging_in),
            Snackbar.LENGTH_INDEFINITE
        )
        val contentLayout =
            snackbar!!.view.findViewById<View>(R.id.snackbar_text).parent as ViewGroup
        val progressBar = ProgressBar(context)
        contentLayout.addView(progressBar, 0)
        snackbar!!.show()
    }

    private fun showErrorSnackBar(message: String) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            binding.coordinatorLayout,
            message,
            Snackbar.LENGTH_LONG
        )
        snackbar!!.show()
    }

    private fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(
                currentFocus?.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    override fun onBackPressed() {
        // do nothing
    }
}
