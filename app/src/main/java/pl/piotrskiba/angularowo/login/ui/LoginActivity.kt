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
import dagger.android.AndroidInjection
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.activities.base.BaseActivity
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.databinding.ActivityLoginBinding
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
        AndroidInjection.inject(this)
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
                is LoginState.Error -> showErrorSnackBar()
                is LoginState.Success -> onSuccessLogin()
                else -> snackbar?.dismiss()
            }
        })
    }

    fun bindViewModel() {
        viewModel = viewModelFactory.obtainViewModel(this)
        val binding: ActivityLoginBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewModel = viewModel
    }

    private fun onSuccessLogin() {
        setResult(Constants.RESULT_CODE_SUCCESS)
        finish()
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

    private fun showErrorSnackBar() {
        snackbar?.dismiss()
        snackbar = Snackbar.make(
            mCoordinatorLayout,
            getString(R.string.login_error_unknown),
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