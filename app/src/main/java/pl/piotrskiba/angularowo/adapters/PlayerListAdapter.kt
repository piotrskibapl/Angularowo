package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.AppViewModel
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.PlayerListAdapter.PlayerViewHolder
import pl.piotrskiba.angularowo.adapters.diffcallbacks.PlayerListDiffCallback
import pl.piotrskiba.angularowo.database.entity.Friend
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.models.PlayerList
import pl.piotrskiba.angularowo.utils.ColorUtils.getColorFromCode
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.RankUtils
import pl.piotrskiba.angularowo.utils.UrlUtils.buildAvatarUrl

class PlayerListAdapter(
        private val context: Context,
        private val mClickListener: PlayerClickListener,
        private val mViewModel: AppViewModel
) : RecyclerView.Adapter<PlayerViewHolder>() {

    private var playerList: PlayerList = PlayerList(ArrayList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.player_list_item, parent, false)

        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playerList.players[position]

        Glide.with(context)
                .load(buildAvatarUrl(player.skinUuid, true))
                .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                .placeholder(R.drawable.default_avatar)
                .into(holder.mPlayerAvatar)

        holder.mPlayerName.text = player.username
        holder.mPlayerRank.text = player.rank.name

        val color = getColorFromCode(context, player.rank.colorCode)
        (holder.mPlayerAvatar.parent as ConstraintLayout).setBackgroundColor(color)

        if (player.isVanished)
            holder.mPlayerVanishIcon.visibility = View.VISIBLE
        else
            holder.mPlayerVanishIcon.visibility = View.INVISIBLE

        val friends = mViewModel.allFriends.value
        if (friends != null && friends.contains(Friend(player.uuid))) {
            holder.mPlayerHeartIcon.visibility = View.VISIBLE
        }
        else {
            holder.mPlayerHeartIcon.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return playerList.players.size
    }

    inner class PlayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.iv_player_avatar)
        lateinit var mPlayerAvatar: ImageView

        @BindView(R.id.tv_player_name)
        lateinit var mPlayerName: TextView

        @BindView(R.id.tv_player_rank)
        lateinit var mPlayerRank: TextView

        @BindView(R.id.iv_vanish_status)
        lateinit var mPlayerVanishIcon: ImageView

        @BindView(R.id.iv_heart)
        lateinit var mPlayerHeartIcon: ImageView

        override fun onClick(view: View) {
            mClickListener.onPlayerClick(view, playerList.players[bindingAdapterPosition])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setPlayerList(playerList: PlayerList) {
        val newPlayerList: MutableList<Player> = ArrayList()
        for (rank in RankUtils.allRanks) {
            for (player in playerList.players) {
                if (player.rank.name == rank.name) newPlayerList.add(player)
            }
        }

        val diffCallback = PlayerListDiffCallback(this.playerList, PlayerList(newPlayerList))
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        this.playerList = PlayerList(newPlayerList)
        diffResult.dispatchUpdatesTo(this)
    }

}