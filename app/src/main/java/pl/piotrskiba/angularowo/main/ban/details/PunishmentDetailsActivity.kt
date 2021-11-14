package pl.piotrskiba.angularowo.main.ban.details

import android.graphics.Bitmap
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.base.ui.OldBaseActivity
import pl.piotrskiba.angularowo.models.Ban
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class PunishmentDetailsActivity : OldBaseActivity() {

    @BindView(R.id.toolbar)
    lateinit var mToolbar: Toolbar

    @BindView(R.id.iv_player_avatar)
    lateinit var mPlayerAvatar: ImageView

    @BindView(R.id.tv_player_name)
    lateinit var mPlayerName: TextView

    @BindView(R.id.tv_punishment_description)
    lateinit var mBanReason: TextView

    @BindView(R.id.iv_punishment_type)
    lateinit var mBanTypeImageView: ImageView

    @BindView(R.id.tv_punishment_type)
    lateinit var mBanTypeTextView: TextView

    @BindView(R.id.tv_punishment_description_full)
    lateinit var mBanFullReason: TextView

    @BindView(R.id.tv_punishment_banner)
    lateinit var mBanBanner: TextView

    @BindView(R.id.tv_punishment_start_date)
    lateinit var mBanStartDate: TextView

    @BindView(R.id.tv_punishment_end_date)
    lateinit var mBanEndDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_punishment_details)

        ButterKnife.bind(this)

        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.punishment_info)

        if (intent.hasExtra(Constants.EXTRA_BAN)) {
            val ban = intent.getSerializableExtra(Constants.EXTRA_BAN) as Ban
            populateBan(ban)
        }
    }

    private fun populateBan(ban: Ban) {
        mPlayerName.text = ban.username

        if (intent.hasExtra(Constants.EXTRA_BITMAP)) {
            val avatarBitmap = intent.getParcelableExtra<Bitmap>(Constants.EXTRA_BITMAP)
            mPlayerAvatar.setImageBitmap(avatarBitmap)
        }

        var reason = ban.reason.toLowerCase(Locale.getDefault()).replace("&8", "")
        when (ban.type) {
            Ban.TYPE_BAN -> {
                mBanTypeImageView.setImageResource(R.drawable.ic_ban)
                if (reason.contains(":"))
                    reason = reason.split(":").toTypedArray()[0]
                mBanReason.text = getString(R.string.ban_description_format, reason)
            }
            Ban.TYPE_MUTE -> {
                mBanTypeImageView.setImageResource(R.drawable.ic_mute)
                mBanReason.text = getString(R.string.mute_description_format, reason)
            }
            Ban.TYPE_WARNING -> {
                mBanTypeImageView.setImageResource(R.drawable.ic_warning)
                mBanReason.text = getString(R.string.warn_description_format, reason)
            }
        }

        mBanTypeTextView.text = ban.type
        mBanBanner.text = ban.banner
        mBanFullReason.text = ban.reason.replace("&8", "")

        val start = Date(ban.banTime.toLong() * 1000)
        val end = Date(ban.expireDate.toLong() * 1000)

        val pattern = "dd.MM.yyyy HH:mm"
        val df: DateFormat = SimpleDateFormat(pattern, Locale.getDefault())

        mBanStartDate.text = df.format(start)

        if (ban.expireDate == 0f)
            mBanEndDate.text = getString(R.string.punishment_expiration_never)
        else
            mBanEndDate.text = df.format(end)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            supportFinishAfterTransition()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}