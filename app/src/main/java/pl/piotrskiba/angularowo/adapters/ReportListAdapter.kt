package pl.piotrskiba.angularowo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import pl.piotrskiba.angularowo.Constants
import pl.piotrskiba.angularowo.R
import pl.piotrskiba.angularowo.adapters.ReportListAdapter.ReportViewHolder
import pl.piotrskiba.angularowo.interfaces.ReportClickListener
import pl.piotrskiba.angularowo.models.ReportList

class ReportListAdapter(private val context: Context, private val mClickListener: ReportClickListener) : RecyclerView.Adapter<ReportViewHolder>() {
    private var reportList: ReportList = ReportList(ArrayList())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportViewHolder {
        val context = parent.context

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.report_list_item, parent, false)

        return ReportViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportViewHolder, position: Int) {
        val report = reportList.reportList[position]

        holder.mPlayerName.text = report.reported
        holder.mReportReason.text = context.getString(R.string.report_reason, report.reason)

        when (report.appreciation) {
            Constants.API_RESPONSE_REPORT_ACCEPTED -> {
                holder.mReportStatus.text = context.getString(R.string.report_status_true)
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_accepted)
            }
            Constants.API_RESPONSE_REPORT_REJECTED -> {
                holder.mReportStatus.text = context.getString(R.string.report_status_false)
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_rejected)
            }
            Constants.API_RESPONSE_REPORT_PENDING -> {
                holder.mReportStatus.text = context.getString(R.string.report_status_awaiting)
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_pending)
            }
            Constants.API_RESPONSE_REPORT_UNCERTAIN -> {
                holder.mReportStatus.text = context.getString(R.string.report_status_uncertain)
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_uncertain)
            }
        }
    }

    override fun getItemCount(): Int {
        return reportList.reportList.size
    }

    inner class ReportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        @BindView(R.id.tv_player_name)
        lateinit var mPlayerName: TextView

        @BindView(R.id.tv_report_reason)
        lateinit var mReportReason: TextView

        @BindView(R.id.tv_report_status)
        lateinit var mReportStatus: TextView

        @BindView(R.id.iv_report_status)
        lateinit var mReportStatusImage: ImageView

        override fun onClick(view: View) {
            mClickListener.onReportClick(view, reportList.reportList[bindingAdapterPosition])
        }

        init {
            ButterKnife.bind(this, itemView)
            itemView.setOnClickListener(this)
        }
    }

    fun setReportList(reportList: ReportList) {
        this.reportList = reportList
        notifyDataSetChanged()
    }

}