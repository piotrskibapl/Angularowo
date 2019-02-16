package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.models.MojangPlayer;
import pl.piotrskiba.angularowo.models.Player;
import pl.piotrskiba.angularowo.models.PlayerList;
import pl.piotrskiba.angularowo.network.MojangAPIClient;
import pl.piotrskiba.angularowo.network.MojangAPIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.PlayerViewHolder> {

    private Context context;
    private PlayerList playerList;

    public PlayerListAdapter(Context context){
        this.context = context;
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

        MojangAPIInterface serverAPIInterface = MojangAPIClient.getRetrofitInstance().create(MojangAPIInterface.class);
        serverAPIInterface.getPlayer(player.getUsername()).enqueue(new Callback<MojangPlayer>() {
            @Override
            public void onResponse(Call<MojangPlayer> call, Response<MojangPlayer> response) {
                if(response.isSuccessful()) {
                    MojangPlayer mojangPlayer = response.body();
                    if(mojangPlayer != null) {
                        Glide.with(context)
                                .load("https://crafatar.com/avatars/" + response.body().getId() + "?size=100")
                                .into(holder.mPlayerAvatar);
                    }
                }
            }

            @Override
            public void onFailure(Call<MojangPlayer> call, Throwable t) {
                t.printStackTrace();
            }
        });

        holder.mPlayerName.setText(player.getUsername());
        holder.mPlayerRank.setText(player.getRank());
    }

    @Override
    public int getItemCount() {
        if(playerList != null)
            return playerList.getPlayers().size();
        else
            return 0;
    }

    class PlayerViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_player_avatar)
        ImageView mPlayerAvatar;

        @BindView(R.id.tv_player_name)
        TextView mPlayerName;

        @BindView(R.id.tv_player_rank)
        TextView mPlayerRank;

        public PlayerViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public void setPlayerList(PlayerList playerList){
        this.playerList = playerList;
        notifyDataSetChanged();
    }
}
