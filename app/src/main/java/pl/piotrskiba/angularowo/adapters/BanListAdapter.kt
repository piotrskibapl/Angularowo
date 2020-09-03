package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.BanListAdapter.BanViewHolder
import pl.piotrskiba.angularowo.adapters.diffcallbacks.BanListDiffCallback
import pl.piotrskiba.angularowo.interfaces.BanClickListener
import pl.piotrskiba.angularowo.models.Ban
import pl.piotrskiba.angularowo.models.BanList
import pl.piotrskiba.angularowo.models.MojangProfile
import pl.piotrskiba.angularowo.network.MojangAPIClient.retrofitInstance
import pl.piotrskiba.angularowo.network.MojangAPIInterface
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.UrlUtils.buildAvatarUrl
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class BanListAdapter(private val context: Context, private val mClickListener: BanClickListener) : RecyclerView.Adapter<BanViewHolder>() {
    private var banList: BanList = BanList(ArrayList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BanViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.ban_list_item, parent, false)

        return BanViewHolder(view)
    }

    override fun onBindViewHolder(holder: BanViewHolder, position: Int) {
        val ban = banList.banList[position]

        holder.mPlayerName.text = ban.username

        var reason = ban.reason.toLowerCase(Locale.getDefault())

        when (ban.type) {
            Ban.TYPE_BAN -> {
                holder.mBanType.setImageResource(R.drawable.ic_ban)

                if (reason.contains(":"))
                    reason = reason.split(":").toTypedArray()[0]

                holder.mBanReason.text = context.getString(R.string.ban_description_format, reason)
            }
            Ban.TYPE_MUTE -> {
                holder.mBanType.setImageResource(R.drawable.ic_mute)
                holder.mBanReason.text = context.getString(R.string.mute_description_format, reason)
            }
            Ban.TYPE_WARNING -> {
                holder.mBanType.setImageResource(R.drawable.ic_warning)
                holder.mBanReason.text = context.getString(R.string.warn_description_format, reason)
            }
        }

        val avatar = ContextCompat.getDrawable(context, R.drawable.default_avatar)
        holder.mPlayerAvatar.setImageDrawable(avatar)
        if (ban.uuid == null) {
            val mojangAPIInterface = retrofitInstance.create(MojangAPIInterface::class.java)

            mojangAPIInterface.getProfile(ban.username).enqueue(object : Callback<MojangProfile?> {
                override fun onResponse(call: Call<MojangProfile?>, response: Response<MojangProfile?>) {
                    if (holder.bindingAdapterPosition == position) {
                        if (response.isSuccessful) {
                            val mojangProfile = response.body()

                            mojangProfile?.run {
                                Glide.with(context)
                                        .load(buildAvatarUrl(mojangProfile.id, true))
                                        .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                                        .placeholder(R.drawable.default_avatar)
                                        .into(holder.mPlayerAvatar)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<MojangProfile?>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        } else {
            Glide.with(context)
                    .load(buildAvatarUrl(ban.uuid, true))
                    .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                    .placeholder(R.drawable.default_avatar)
                    .into(holder.mPlayerAvatar)
        }
    }

    override fun getItemCount(): Int {
        return banList.banList.size
    }

    inner class BanViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.main_layout)
        lateinit var mMainLayout: ConstraintLayout

        @BindView(R.id.iv_player_avatar)
        lateinit var mPlayerAvatar: ImageView

        @BindView(R.id.tv_player_name)
        lateinit var mPlayerName: TextView

        @BindView(R.id.tv_ban_description)
        lateinit var mBanReason: TextView

        @BindView(R.id.iv_ban_type)
        lateinit var mBanType: ImageView

        override fun onClick(view: View) {
            mClickListener.onBanClick(view, banList.banList[bindingAdapterPosition])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
            mMainLayout.tag = this
        }
    }

    fun setBanList(banList: BanList) {
        val diffCallback = BanListDiffCallback(this.banList, banList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.banList = banList
        diffResult.dispatchUpdatesTo(this)
    }
}