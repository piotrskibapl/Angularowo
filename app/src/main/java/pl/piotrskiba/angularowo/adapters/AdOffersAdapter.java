package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.AdOfferClickListener;
import pl.piotrskiba.angularowo.models.AdOffer;
import pl.piotrskiba.angularowo.utils.TextUtils;

public class AdOffersAdapter extends RecyclerView.Adapter<AdOffersAdapter.AdOfferViewHolder> {

    private final Context context;
    private final AdOfferClickListener mClickListener;

    private List<AdOffer> mAdOfferList;

    public AdOffersAdapter(Context context, AdOfferClickListener listener){
        this.context = context;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public AdOfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.ad_offer_list_item, parent, false);

        return new AdOfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdOfferViewHolder holder, int position) {
        AdOffer adOffer = mAdOfferList.get(position);

        holder.mCoins.setText(String.valueOf(adOffer.getPoints()));

        if(adOffer.getTimeleft() > 0) {
            holder.mCoins.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_disabled, 0, 0, 0);
            holder.mCoins.setTextColor(ContextCompat.getColor(context, R.color.color_coin_disabled));

            holder.mTime.setText(TextUtils.formatApproximateTimeleft(adOffer.getTimeleft()));
            holder.mTime.setVisibility(View.VISIBLE);
        }
        else {
            holder.mCoins.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin, 0, 0, 0);
            holder.mCoins.setTextColor(ContextCompat.getColor(context, R.color.color_coin));

            holder.mTime.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(mAdOfferList == null)
            return 0;
        else
            return mAdOfferList.size();
    }

    public class AdOfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_coins)
        TextView mCoins;

        @BindView(R.id.tv_time)
        TextView mTime;

        AdOfferViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onAdOfferClick(view, mAdOfferList.get(getAdapterPosition()));
        }
    }

    public void setAdOfferList(List<AdOffer> list){
        mAdOfferList = list;
        notifyDataSetChanged();
    }
}
