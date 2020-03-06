package pl.piotrskiba.angularowo.activities

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.preference.PreferenceManager
import butterknife.BindView
import butterknife.ButterKnife
import com.alimuzaffar.lib.pin.PinEntryEditText
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.models.AccessToken
import pl.piotrskiba.angularowo.network.ServerAPIClient
import pl.piotrskiba.angularowo.network.ServerAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.ServerAPIInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LoginActivity : AppCompatActivity() {

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

        setSupportActionBar(mToolbar)

        context = this

        accessTokenPeet.setOnPinEnteredListener { str: CharSequence ->
            val pin = str.toString()

            accessTokenPeet.setText("")
            closeKeyboard()

            // show loading snackbar
            val snackbar = Snackbar.make(mCoordinatorLayout, getString(R.string.logging_in), Snackbar.LENGTH_INDEFINITE)
            val contentLay = snackbar.view.findViewById<View>(R.id.snackbar_text).parent as ViewGroup
            val item = ProgressBar(context)
            contentLay.addView(item, 0)
            snackbar.show()

            val serverAPIInterface = retrofitInstance.create(ServerAPIInterface::class.java)
            serverAPIInterface.registerDevice(ServerAPIClient.API_KEY, pin).enqueue(object : Callback<AccessToken?> {
                override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
                    if (response.body() != null) {
                        val accessToken = response.body()

                        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
                        val editor = sharedPreferences.edit()
                        editor.putString(getString(R.string.pref_key_nickname), accessToken!!.username)
                        editor.putString(getString(R.string.pref_key_access_token), accessToken.accessToken)
                        editor.commit()

                        setResult(Constants.RESULT_CODE_SUCCESS)
                        finish()
                    }
                    else if (response.errorBody() != null) {
                        val gson = Gson()
                        val adapter = gson.getAdapter(AccessToken::class.java)

                        try {
                            val accessToken = adapter.fromJson(response.errorBody()!!.string())

                            if (accessToken.message == getString(R.string.login_api_response_code_not_found))
                                Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_code_not_found), Snackbar.LENGTH_LONG).show()
                            else if (accessToken.message == getString(R.string.login_api_response_code_expired))
                                Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_code_expired), Snackbar.LENGTH_LONG).show()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                }

                override fun onFailure(call: Call<AccessToken?>, t: Throwable) {
                    Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_unknown), Snackbar.LENGTH_LONG).show()
                    t.printStackTrace()
                }
            })
        }
    }

    private fun closeKeyboard() {
        val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (currentFocus != null) {
            inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }

    override fun onBackPressed() {
        // do nothing
    }
}