package pl.piotrskiba.angularowo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApplicationLockedActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;

    @BindView(R.id.tv_body)
    TextView mBodyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_locked);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setDefaults(R.xml.remote_config_default_values);

        String title = firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_TITLE_KEY);
        String body = firebaseRemoteConfig.getString(Constants.REMOTE_CONFIG_APP_LOCK_BODY_KEY);

        title = title.replace("\\n", "\n");
        body = body.replace("\\n", "\n");

        mTitleTextView.setText(title);
        mBodyTextView.setText(body);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }
}
