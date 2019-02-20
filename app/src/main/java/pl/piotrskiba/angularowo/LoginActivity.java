package pl.piotrskiba.angularowo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.AccessToken;
import pl.piotrskiba.angularowo.network.ServerAPIClient;
import pl.piotrskiba.angularowo.network.ServerAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.inputmethod.InputMethodManager;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.peet_accesstoken)
    PinEntryEditText accessTokenPeet;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        context = this;

        accessTokenPeet.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str) {
                String pin = str.toString();

                accessTokenPeet.setText("");
                closeKeyboard();

                ServerAPIInterface serverAPIInterface = ServerAPIClient.getRetrofitInstance().create(ServerAPIInterface.class);
                serverAPIInterface.registerDevice(ServerAPIClient.API_KEY, pin).enqueue(new Callback<AccessToken>() {
                    @Override
                    public void onResponse(Call<AccessToken> call, Response<AccessToken> response) {
                        AccessToken accessToken = response.body();

                        if (accessToken != null) {
                            if (response.code() == 200) {
                                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putString(getString(R.string.pref_key_nickname), accessToken.getUsername());
                                editor.putString(getString(R.string.pref_key_access_token), accessToken.getAccessToken());

                                editor.commit();

                                finish();
                            } else {
                                if (accessToken.getMessage().equals(getString(R.string.login_api_response_code_not_found))) {
                                    Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_code_not_found), Snackbar.LENGTH_LONG).show();
                                } else if (accessToken.getMessage().equals(getString(R.string.login_api_response_code_expired))) {
                                    Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_code_expired), Snackbar.LENGTH_LONG).show();
                                }
                            }
                        }
                        else{
                            Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_unknown), Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AccessToken> call, Throwable t) {
                        Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_unknown), Snackbar.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    private void closeKeyboard(){
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(getCurrentFocus() != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }


}
