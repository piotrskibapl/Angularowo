package pl.piotrskiba.angularowo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.Ban;

public class BanDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.iv_player_avatar)
    ImageView mPlayerAvatar;

    @BindView(R.id.tv_player_name)
    TextView mPlayerName;

    @BindView(R.id.tv_ban_description)
    TextView mBanReason;

    @BindView(R.id.iv_ban_type)
    ImageView mBanTypeImageView;

    //

    @BindView(R.id.tv_ban_type)
    TextView mBanTypeTextView;

    @BindView(R.id.tv_ban_description_full)
    TextView mBanFullReason;

    @BindView(R.id.tv_ban_banner)
    TextView mBanBanner;

    @BindView(R.id.tv_ban_start_date)
    TextView mBanStartDate;

    @BindView(R.id.tv_ban_end_date)
    TextView mBanEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ban_details);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle(R.string.ban_info);
        }

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(Constants.EXTRA_BAN)){
            Ban ban = (Ban) parentIntent.getSerializableExtra(Constants.EXTRA_BAN);
            populateBan(ban);
        }
    }

    private void populateBan(Ban ban) {
        mPlayerName.setText(ban.getUsername());

        Intent parentIntent = getIntent();
        if(parentIntent.hasExtra(Constants.EXTRA_BITMAP)){
            Bitmap avatarBitmap = parentIntent.getParcelableExtra(Constants.EXTRA_BITMAP);
            mPlayerAvatar.setImageBitmap(avatarBitmap);
        }

        String reason = ban.getReason().toLowerCase();
        switch(ban.getType()){
            case Ban.TYPE_BAN:
                mBanTypeImageView.setImageResource(R.drawable.ic_ban);
                if (reason.contains(":"))
                    reason = reason.split(":")[0];
                mBanReason.setText(getString(R.string.ban_description_format, reason));
            case Ban.TYPE_MUTE:
                mBanTypeImageView.setImageResource(R.drawable.ic_mute);
                mBanReason.setText(getString(R.string.mute_description_format, reason));
            case Ban.TYPE_WARNING:
                mBanTypeImageView.setImageResource(R.drawable.ic_warning);
                mBanReason.setText(getString(R.string.warn_description_format, reason));
        }

        mBanTypeTextView.setText(ban.getType());
        mBanBanner.setText(ban.getBanner());
        mBanFullReason.setText(ban.getReason());

        Date start = new Date((long) ban.getBanTime()*1000);
        Date end = new Date((long) ban.getExpireDate()*1000);

        String pattern = "dd.MM.yyyy HH:mm";
        DateFormat df = new SimpleDateFormat(pattern);

        mBanStartDate.setText(df.format(start));
        if(ban.getExpireDate() == 0){
            mBanEndDate.setText(getString(R.string.ban_expiration_never));
        }
        else {
            mBanEndDate.setText(df.format(end));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
