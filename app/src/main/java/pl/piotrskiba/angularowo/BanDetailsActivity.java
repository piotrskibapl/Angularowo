package pl.piotrskiba.angularowo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.models.Ban;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    @BindView(R.id.tv_ban_type)
    TextView mBanTypeTextView;

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
        if(ban.getType().equals(Ban.TYPE_BAN)){
            mBanTypeImageView.setImageResource(R.drawable.ic_ban);
            if(reason.contains(":"))
                reason = reason.split(":")[0];
            mBanReason.setText(getString(R.string.ban_description_format, reason));
        }
        else if(ban.getType().equals(Ban.TYPE_MUTE)){
            mBanTypeImageView.setImageResource(R.drawable.ic_mute);
            mBanReason.setText(getString(R.string.mute_description_format, reason));
        }
        else if(ban.getType().equals(Ban.TYPE_WARNING)){
            mBanTypeImageView.setImageResource(R.drawable.ic_warning);
            mBanReason.setText(getString(R.string.warn_description_format, reason));
        }

        mBanTypeTextView.setText(ban.getType());
    }
}
