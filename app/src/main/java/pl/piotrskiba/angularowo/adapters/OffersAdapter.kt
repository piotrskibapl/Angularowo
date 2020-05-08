package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import pl.piotrskiba.angularowo.IntegerVersionSignature
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.OffersAdapter.OfferViewHolder
import pl.piotrskiba.angularowo.interfaces.OfferClickListener
import pl.piotrskiba.angularowo.models.Offer
import pl.piotrskiba.angularowo.utils.GlideUtils.getSignatureVersionNumber
import pl.piotrskiba.angularowo.utils.TextUtils.formatApproximateTimeLeft

class OffersAdapter(private val context: Context, private val mClickListener: OfferClickListener) : RecyclerView.Adapter<OfferViewHolder>() {
    private var mOfferList: List<Offer> = ArrayList()
    private var mCoins = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.offer_list_item, parent, false)

        return OfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = mOfferList[position]

        Glide.with(context)
                .load(offer.imageUrl)
                .fitCenter()
                .signature(IntegerVersionSignature(getSignatureVersionNumber(5)))
                .into(holder.mOfferImage)

        holder.mOfferTitle.text = offer.title
        holder.mOfferDescription.text = offer.description
        holder.mOfferPrice.text = offer.price.toString()

        if (offer.timeleft > 0 || offer.price > mCoins) {
            holder.mOfferImage.setColorFilter(Color.argb(224, 0, 0, 0))
            holder.mOfferPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_disabled, 0, 0, 0)

            holder.mOfferPrice.setTextColor(ContextCompat.getColor(context, R.color.color_coin_disabled))
            holder.mOfferTitle.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_disabled))
            holder.mOfferDescription.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_disabled))

            if (offer.timeleft > 0) {
                holder.mOfferTime.text = formatApproximateTimeLeft(offer.timeleft)
                holder.mOfferTime.visibility = View.VISIBLE
            }
            else
                holder.mOfferTime.visibility = View.INVISIBLE
        } else {
            holder.mOfferImage.setColorFilter(Color.argb(128, 0, 0, 0))
            holder.mOfferPrice.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin, 0, 0, 0)

            holder.mOfferPrice.setTextColor(ContextCompat.getColor(context, R.color.color_coin))
            holder.mOfferTitle.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_default))
            holder.mOfferDescription.setTextColor(ContextCompat.getColor(context, R.color.offer_text_color_default))

            holder.mOfferTime.visibility = View.INVISIBLE
        }
    }

    override fun getItemCount(): Int {
        return mOfferList.size
    }

    inner class OfferViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.offer_image)
        lateinit var mOfferImage: ImageView

        @BindView(R.id.offer_title)
        lateinit var mOfferTitle: TextView

        @BindView(R.id.offer_description)
        lateinit var mOfferDescription: TextView

        @BindView(R.id.offer_time)
        lateinit var mOfferTime: TextView

        @BindView(R.id.offer_price)
        lateinit var mOfferPrice: TextView

        override fun onClick(view: View) {
            mClickListener.onOfferClick(view, mOfferList[bindingAdapterPosition])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setOfferList(list: ArrayList<Offer>, coins: Int) {
        mOfferList = list
        mCoins = coins

        notifyDataSetChanged()
    }

}