package pl.piotrskiba.angularowo.login.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import butterknife.BindView
import butterknife.ButterKnife
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.base.ui.BaseActivity
import pl.piotrskiba.angularowo.databinding.ActivityLoginBinding
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.login.viewmodel.LoginViewModel
import javax.inject.Inject

class LoginActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: LoginViewModel

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.coordinatorLayout)
    lateinit var mCoordinatorLayout: CoordinatorLayout

    @BindView(R.id.peet_accesstoken)
    lateinit var accessTokenPeet: PinEntryEditText

    private lateinit var context: Context

    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindViewModel()
        ButterKnife.bind(this)
        setSupportActionBar(mToolbar)

        context = this

        accessTokenPeet.setOnPinEnteredListener {
            viewModel.onPinEntered(it.toString())
            accessTokenPeet.setText("")
            closeKeyboard()
            showLoadingSnackBar()
        }

        viewModel.loginState.observe(this, { loginState ->
            when (loginState) {
                is LoginState.Loading -> showLoadingSnackBar()
                is LoginState.Success -> onLoginSuccess()
                is LoginState.Error -> onLoginError(loginState.error)
                else -> snackbar?.dismiss()
            }
        })
    }

    private fun bindViewModel() {
        viewModel = viewModelFactory.obtainViewModel(this)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
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
            mCoordinatorLayout,
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
            mCoordinatorLayout,
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