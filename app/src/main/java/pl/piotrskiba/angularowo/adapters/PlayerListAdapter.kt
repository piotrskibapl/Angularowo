package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.PlayerListAdapter.PlayerViewHolder
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener
import pl.piotrskiba.angularowo.models.Player
import pl.piotrskiba.angularowo.models.PlayerList
import pl.piotrskiba.angularowo.utils.ColorUtils.getColorFromCode
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.RankUtils.allRanks
import pl.piotrskiba.angularowo.utils.UrlUtils.buildAvatarUrl

class PlayerListAdapter(private val context: Context, private val mClickListener: PlayerClickListener) : RecyclerView.Adapter<PlayerViewHolder>() {
    private var playerList: PlayerList = PlayerList(ArrayList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.player_list_item, parent, false)

        return PlayerViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlayerViewHolder, position: Int) {
        val player = playerList.players[position]

        if (player.uuid != null) {
            Glide.with(context)
                    .load(buildAvatarUrl(player.uuid, true))
                    .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                    .placeholder(R.drawable.default_avatar)
                    .into(holder.mPlayerAvatar)
        } else {
            holder.mPlayerAvatar.setImageDrawable(context.resources.getDrawable(R.drawable.default_avatar))
        }

        holder.mPlayerName.text = player.username
        holder.mPlayerRank.text = player.getRank().name

        val color = getColorFromCode(context, player.getRank().colorCode)
        (holder.mPlayerAvatar.parent as ConstraintLayout).setBackgroundColor(color)

        if (player.vanished)
            holder.mPlayerVanishIcon.visibility = View.VISIBLE
        else
            holder.mPlayerVanishIcon.visibility = View.INVISIBLE
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

        override fun onClick(view: View) {
            val pos = adapterPosition
            mClickListener.onPlayerClick(view, playerList.players[pos])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setPlayerList(playerList: PlayerList) {
        val sorted: MutableList<Player> = ArrayList()
        if (playerList.players.isNotEmpty()) {
            val ranks = allRanks
            for (rank in ranks) {
                for (player in playerList.players) {
                    if (player.getRank().name == rank.name) sorted.add(player)
                }
            }
        }
        this.playerList = PlayerList(sorted)
        notifyDataSetChanged()
    }

}