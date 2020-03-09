package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.AdOffersAdapter.AdOfferViewHolder
import pl.piotrskiba.angularowo.interfaces.AdOfferClickListener
import pl.piotrskiba.angularowo.models.AdOffer
import pl.piotrskiba.angularowo.utils.TextUtils.formatApproximateTimeLeft

class AdOffersAdapter(private val context: Context, private val mClickListener: AdOfferClickListener) : RecyclerView.Adapter<AdOfferViewHolder>() {
    private var mAdOfferList: List<AdOffer> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdOfferViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.ad_offer_list_item, parent, false)

        return AdOfferViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdOfferViewHolder, position: Int) {
        val adOffer = mAdOfferList[position]

        holder.mCoins.text = adOffer.points.toString()

        if (adOffer.timeleft > 0) {
            holder.mCoins.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin_disabled, 0, 0, 0)
            holder.mCoins.setTextColor(ContextCompat.getColor(context, R.color.color_coin_disabled))

            holder.mTime.text = formatApproximateTimeLeft(adOffer.timeleft)
            holder.mTime.visibility = View.VISIBLE
        } else {
            holder.mCoins.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_coin, 0, 0, 0)
            holder.mCoins.setTextColor(ContextCompat.getColor(context, R.color.color_coin))

            holder.mTime.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return mAdOfferList.size
    }

    inner class AdOfferViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.tv_coins)
        lateinit var mCoins: TextView

        @BindView(R.id.tv_time)
        lateinit var mTime: TextView

        override fun onClick(view: View) {
            mClickListener.onAdOfferClick(view, mAdOfferList[adapterPosition])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setAdOfferList(list: List<AdOffer>) {
        mAdOfferList = list
        notifyDataSetChanged()
    }

}