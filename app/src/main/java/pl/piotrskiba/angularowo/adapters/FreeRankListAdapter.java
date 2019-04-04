package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.FreeRankClickListener;
import pl.piotrskiba.angularowo.models.Reward;

public class FreeRankListAdapter extends RecyclerView.Adapter<FreeRankListAdapter.FreeRankViewHolder> {

    private List<Reward> mRewardList;

    private FreeRankClickListener mClickListener;

    public FreeRankListAdapter(FreeRankClickListener listener){
        mClickListener = listener;
    }

    @NonNull
    @Override
    public FreeRankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.free_rank_list_item, parent, false);

        return new FreeRankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FreeRankViewHolder holder, int position) {
        Reward reward = mRewardList.get(position);

        holder.mRewardImage.setImageResource(reward.getImageResource());
        holder.mRewardTitle.setText(reward.getTitle());
        holder.mRewardDescription.setText(reward.getDescription());
    }

    @Override
    public int getItemCount() {
        if(mRewardList == null)
            return 0;
        else
            return mRewardList.size();
    }

    public class FreeRankViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.reward_image)
        ImageView mRewardImage;

        @BindView(R.id.reward_title)
        TextView mRewardTitle;

        @BindView(R.id.reward_description)
        TextView mRewardDescription;

        public FreeRankViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onFreeRankClick(view, mRewardList.get(getAdapterPosition()));
        }
    }

    public void setRewardList(List<Reward> list){
        mRewardList = list;
        notifyDataSetChanged();
    }
}
