package pl.piotrskiba.angularowo.login.ui

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import butterknife.BindView
import butterknife.ButterKnife
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.google.android.material.snackbar.Snackbar
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.activities.base.BaseActivity
import pl.piotrskiba.angularowo.base.di.obtainViewModel
import pl.piotrskiba.angularowo.login.viewmodel.LoginViewModel

class LoginActivity : BaseActivity() {

    private lateinit var viewModel: LoginViewModel

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.coordinatorLayout)
    lateinit var mCoordinatorLayout: CoordinatorLayout

    @BindView(R.id.peet_accesstoken)
    lateinit var accessTokenPeet: PinEntryEditText

    private lateinit var context: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        ButterKnife.bind(this)
        viewModel = viewModelFactory.obtainViewModel(this)
        setSupportActionBar(mToolbar)

        context = this

        accessTokenPeet.setOnPinEnteredListener {
            viewModel.onPinEntered(it.toString())
            accessTokenPeet.setText("")
            closeKeyboard()
            showLoadingSnackBar()
        }
        /*accessTokenPeet.setOnPinEnteredListener { pin: CharSequence ->
            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.registerDevice(ServerAPIClient.API_KEY, pin)
                .enqueue(object : Callback<AccessToken?> {
                    override fun onResponse(
                        call: Call<AccessToken?>,
                        response: Response<AccessToken?>
                    ) {
                        var accessToken = response.body()

                        if (accessToken != null) {
                            val preferenceUtils = PreferenceUtils(context)
                            preferenceUtils.uuid = accessToken.uuid
                            preferenceUtils.username = accessToken.username
                            preferenceUtils.accessToken = accessToken.accessToken

                            AnalyticsUtils().logLogin(
                                accessToken.uuid,
                                accessToken.username
                            )

                            setResult(Constants.RESULT_CODE_SUCCESS)
                            finish()
                        } else if (response.errorBody() != null) {
                            val gson = Gson()
                            val adapter = gson.getAdapter(AccessToken::class.java)

                            try {
                                accessToken = adapter.fromJson(response.errorBody()?.string())

                                if (accessToken.message == getString(R.string.login_api_response_code_not_found))
                                    Snackbar.make(
                                        mCoordinatorLayout,
                                        getString(R.string.login_error_code_not_found),
                                        Snackbar.LENGTH_LONG
                                    ).show()
                                else if (accessToken.message == getString(R.string.login_api_response_code_expired))
                                    Snackbar.make(
                                        mCoordinatorLayout,
                                        getString(R.string.login_error_code_expired),
                                        Snackbar.LENGTH_LONG
                                    ).show()

                                AnalyticsUtils().logLoginError(accessToken.message)
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onFailure(call: Call<AccessToken?>, t: Throwable) {
                        Snackbar.make(
                            mCoordinatorLayout,
                            getString(R.string.login_error_unknown),
                            Snackbar.LENGTH_LONG
                        ).show()
                        t.printStackTrace()
                    }
                })
        }*/
    }

    private fun showLoadingSnackBar() {
        val snackbar = Snackbar.make(
            mCoordinatorLayout,
            getString(R.string.logging_in),
            Snackbar.LENGTH_INDEFINITE
        )
        val contentLayout = snackbar.view.findViewById<View>(R.id.snackbar_text).parent as ViewGroup
        val progressBar = ProgressBar(context)
        contentLayout.addView(progressBar, 0)
        snackbar.show()
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