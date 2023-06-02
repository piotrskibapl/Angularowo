package pl.piotrskiba.angularowo.login.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.extensions.changeStartDestination
import pl.piotrskiba.angularowo.base.ui.BaseFragment
import pl.piotrskiba.angularowo.databinding.FragmentLoginBinding
import pl.piotrskiba.angularowo.domain.login.model.AccessTokenError
import pl.piotrskiba.angularowo.login.model.LoginState
import pl.piotrskiba.angularowo.login.viewmodel.LoginViewModel

class LoginFragment : BaseFragment<LoginViewModel>(LoginViewModel::class) {

    private lateinit var binding: FragmentLoginBinding
    private var snackbar: Snackbar? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val binding = setupBinding(inflater, container)
        binding.peetAccesstoken.setOnPinEnteredListener {
            viewModel.onPinEntered(it.toString())
            binding.peetAccesstoken.setText("")
            closeKeyboard()
        }
        viewModel.loginState.observe(viewLifecycleOwner) { loginState ->
            when (loginState) {
                is LoginState.Loading -> showLoadingSnackBar()
                is LoginState.Success -> onLoginSuccess()
                is LoginState.Error -> onLoginError(loginState.error)
                else -> snackbar?.dismiss()
            }
        }
        return binding.root
    }

    private fun setupBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentLoginBinding {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        return binding
    }

    private fun closeKeyboard() {
        val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (requireActivity().currentFocus != null) {
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS,
            )
        }
    }

    private fun onLoginSuccess() {
        findNavController().changeStartDestination(R.id.mainScreenFragment)
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
}
