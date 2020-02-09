package pl.piotrskiba.angularowo.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.piotrskiba.angularowo.IntegerVersionSignature;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.OfferClickListener;
import pl.piotrskiba.angularowo.models.Offer;
import pl.piotrskiba.angularowo.utils.GlideUtils;
import pl.piotrskiba.angularowo.utils.TextUtils;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    private final Context context;
    private final OfferClickListener mClickListener;

    private List<Offer> mOfferList;
    private int mCoins;

    public OffersAdapter(Context context, OfferClickListener listener){
        this.context = context;
        mClickListener = listener;
    }

    @NonNull
    @Override
    public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.offer_list_item, parent, false);

        return new OffersAdapter.OfferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OffersAdapter.OfferViewHolder holder, int position) {
        Offer offer = mOfferList.get(position);

        Glide.with(context)
                .load(offer.getImageUrl())
                .fitCenter()
                .signature(new IntegerVersionSignature(GlideUtils.getSignatureVersionNumber(5)))
                .into(holder.mOfferImage);

        holder.mOfferTitle.setText(offer.getTitle());
        holder.mOfferDescription.setText(offer.getDescription());
        holder.mOfferPrice.setText(String.valueOf(offer.getPrice()));

        if(offer.getTimeleft() > 0 || offer.getPrice() > mCoins){
            holder.mOfferImage.setColorFilter(Color.argb(224, 0, 0, 0));

            holder.mOfferPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_disabled, 0, 0, 0);
            holder.mOfferPrice.setTextColor(ContextCompat.getColor(context, R.color.color_coin_disabled));

            holder.mOfferTitle.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_disabled));
            holder.mOfferDescription.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_disabled));

            if(offer.getTimeleft() > 0) {
                holder.mOfferTime.setText(TextUtils.formatApproximateTimeleft(offer.getTimeleft()));
                holder.mOfferTime.setVisibility(View.VISIBLE);
            }
            else
                holder.mOfferTime.setVisibility(View.INVISIBLE);
        }
        else{
            holder.mOfferImage.setColorFilter(Color.argb(128, 0, 0, 0));

            holder.mOfferPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin, 0, 0, 0);
            holder.mOfferPrice.setTextColor(ContextCompat.getColor(context, R.color.color_coin));

            holder.mOfferTitle.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_default));
            holder.mOfferDescription.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_default));

            holder.mOfferTime.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if(mOfferList == null)
            return 0;
        else
            return mOfferList.size();
    }

    public class OfferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.offer_image)
        ImageView mOfferImage;

        @BindView(R.id.offer_title)
        TextView mOfferTitle;

        @BindView(R.id.offer_description)
        TextView mOfferDescription;

        @BindView(R.id.offer_time)
        TextView mOfferTime;

        @BindView(R.id.offer_price)
        TextView mOfferPrice;

        OfferViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mClickListener.onOfferClick(view, mOfferList.get(getAdapterPosition()));
        }
    }

    public void setOfferList(List<Offer> list, int coins){
        mOfferList = list;
        mCoins = coins;
        notifyDataSetChanged();
    }
}
