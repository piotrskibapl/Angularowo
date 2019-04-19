package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.IntegerVersionSignature;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.BanClickListener;
import pl.piotrskiba.angularowo.models.Ban;
import pl.piotrskiba.angularowo.models.BanList;
import pl.piotrskiba.angularowo.models.MojangProfile;
import pl.piotrskiba.angularowo.network.MojangAPIClient;
import pl.piotrskiba.angularowo.network.MojangAPIInterface;
import pl.piotrskiba.angularowo.utils.GlideUtils;
import pl.piotrskiba.angularowo.utils.UrlUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BanListAdapter extends RecyclerView.Adapter<BanListAdapter.BanViewHolder> {

    private Context context;
    private BanList banList;

    private BanClickListener mClickListener;

    public BanListAdapter(Context context, BanClickListener listener){
        this.context = context;
        this.mClickListener = listener;
    }

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

        String reason = ban.getReason().toLowerCase();
        if(ban.getType().equals(Ban.TYPE_BAN)){
            holder.mBanType.setImageResource(R.drawable.ic_ban);
            if(reason.contains(":"))
                reason = reason.split(":")[0];
            holder.mBanReason.setText(context.getString(R.string.ban_description_format, reason));
        }
        else if(ban.getType().equals(Ban.TYPE_MUTE)){
            holder.mBanType.setImageResource(R.drawable.ic_mute);
            holder.mBanReason.setText(context.getString(R.string.mute_description_format, reason));
        }
        else if(ban.getType().equals(Ban.TYPE_WARNING)){
            holder.mBanType.setImageResource(R.drawable.ic_warning);
            holder.mBanReason.setText(context.getString(R.string.warn_description_format, reason));
        }

        holder.mPlayerAvatar.setImageDrawable(context.getResources().getDrawable(R.drawable.default_avatar));
;
        MojangAPIInterface mojangAPIInterface = MojangAPIClient.getRetrofitInstance().create(MojangAPIInterface.class);
        mojangAPIInterface.getProfile(ban.getUsername()).enqueue(new Callback<MojangProfile>() {
            @Override
            public void onResponse(Call<MojangProfile> call, Response<MojangProfile> response) {
                if(holder.getAdapterPosition() == position) {
                    if (response.isSuccessful() && response.body() != null) {
                        Glide.with(context)
                                .load(UrlUtils.buildAvatarUrl(response.body().getId(), true))
                                .signature(new IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
                                .into(holder.mPlayerAvatar);
                    }
                }
            }

            @Override
            public void onFailure(Call<MojangProfile> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        if(banList != null && banList.getBanList() != null)
            return banList.getBanList().size();
        else
            return 0;
    }

    public class BanViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.main_layout)
        ConstraintLayout mMainLayout;

        @BindView(R.id.iv_player_avatar)
        public ImageView mPlayerAvatar;

        @BindView(R.id.tv_player_name)
        TextView mPlayerName;

        @BindView(R.id.tv_ban_description)
        TextView mBanReason;

        @BindView(R.id.iv_ban_type)
        ImageView mBanType;

        public BanViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);

            mMainLayout.setTag(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            mClickListener.onBanClick(view, banList.getBanList().get(pos));
        }
    }

    public void setBanList(BanList banList){
        this.banList = banList;
        notifyDataSetChanged();
    }
}
