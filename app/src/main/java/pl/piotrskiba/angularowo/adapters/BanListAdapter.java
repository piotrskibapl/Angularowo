package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.models.Ban;
import pl.piotrskiba.angularowo.models.BanList;

public class BanListAdapter extends RecyclerView.Adapter<BanListAdapter.BanViewHolder> {

    private BanList banList;

    @NonNull
    @Override
    public BanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ban_list_item, parent, false);

        return new BanViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanViewHolder holder, int position) {
        Ban ban = banList.getBanList().get(position);

        holder.mPlayerName.setText(ban.getUsername());
        holder.mBanReason.setText(ban.getReason());
    }

    @Override
    public int getItemCount() {
        if(banList != null && banList.getBanList() != null)
            return banList.getBanList().size();
        else
            return 0;
    }

    class BanViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_player_avatar)
        ImageView mPlayerAvatar;

        @BindView(R.id.tv_player_name)
        TextView mPlayerName;

        @BindView(R.id.tv_ban_description)
        TextView mBanReason;

        @BindView(R.id.iv_ban_type)
        ImageView mBanType;

        public BanViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

    public void setBanList(BanList banList){
        this.banList = banList;
        notifyDataSetChanged();
    }
}
