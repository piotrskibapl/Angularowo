package pl.piotrskiba.angularowo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

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

        setSupportActionBar(mToolbar);

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
                        if (response.body() != null) {
                            AccessToken accessToken = response.body();

                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(getString(R.string.pref_key_nickname), accessToken.getUsername());
                                editor.putString(getString(R.string.pref_key_access_token), accessToken.getAccessToken());

                            editor.commit();

                            setResult(Constants.RESULT_CODE_SUCCESS);
                            finish();
                        }
                        else if(response.errorBody() != null) {
                            Gson gson = new Gson();
                            TypeAdapter<AccessToken> adapter = gson.getAdapter(AccessToken.class);
                            try {
                                AccessToken accessToken = adapter.fromJson(response.errorBody().string());

                                if (accessToken.getMessage().equals(getString(R.string.login_api_response_code_not_found))) {
                                    Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_code_not_found), Snackbar.LENGTH_LONG).show();
                                } else if (accessToken.getMessage().equals(getString(R.string.login_api_response_code_expired))) {
                                    Snackbar.make(mCoordinatorLayout, getString(R.string.login_error_code_expired), Snackbar.LENGTH_LONG).show();
                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
