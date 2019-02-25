package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.PlayerClickListener;
import pl.piotrskiba.angularowo.models.Player;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.utils.RankUtils;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private Context context;
    private PlayerList playerList;

    private PlayerClickListener mClickListener;

    public PlayerListAdapter(Context context, PlayerClickListener clickListener){
        this.context = context;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public PlayerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.player_list_item, parent, false);

        return new PlayerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlayerViewHolder holder, int position) {
        Player player = playerList.getPlayers().get(position);

        if(player.getUuid() != null) {
            Glide.with(context)
                    .load("https://crafatar.com/avatars/" + player.getUuid() + "?size=100")
                    .into(holder.mPlayerAvatar);
        }
        else{
            holder.mPlayerAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.default_avatar));
        }

        holder.mPlayerName.setText(player.getUsername());
        holder.mPlayerRank.setText(player.getRank());

        int color_id = RankUtils.getRankColorId(player.getRank());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            ((ConstraintLayout) holder.mPlayerAvatar.getParent()).setBackground(context.getResources().getDrawable(color_id));
        }
        else{
            ((ConstraintLayout) holder.mPlayerAvatar.getParent()).setBackgroundDrawable(context.getResources().getDrawable(color_id));
        }

        if(player.isVanished())
            holder.mPlayerVanishIcon.setVisibility(View.VISIBLE);
        else
            holder.mPlayerVanishIcon.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        if(playerList != null)
            return playerList.getPlayers().size();
        else
            return 0;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_player_avatar)
        ImageView mPlayerAvatar;

        @BindView(R.id.tv_player_name)
        TextView mPlayerName;

        @BindView(R.id.tv_player_rank)
        TextView mPlayerRank;

        @BindView(R.id.iv_vanish_status)
        ImageView mPlayerVanishIcon;

        public PlayerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            mClickListener.onPlayerClick(playerList.getPlayers().get(pos));
        }
    }

    public void setPlayerList(PlayerList playerList){
        List<Player> sorted = new ArrayList<>();
        String[] ranks_team = context.getResources().getStringArray(R.array.ranks_team);
        String[] ranks_other = context.getResources().getStringArray(R.array.ranks_other);

        if(playerList.getPlayers() != null && !playerList.getPlayers().isEmpty()) {
            for (String rank : ranks_team) {
                for (Player player : playerList.getPlayers()) {
                    if (player.getRank().equals(rank))
                        sorted.add(player);
                }
            }
            for (String rank : ranks_other) {
                for (Player player : playerList.getPlayers()) {
                    if (player.getRank().equals(rank))
                        sorted.add(player);
                }
            }
        }

        this.playerList = new PlayerList(sorted);
        notifyDataSetChanged();
    }
}
