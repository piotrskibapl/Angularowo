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
import pl.piotrskiba.angularowo.Constants;
import pl.piotrskiba.angularowo.R;
import pl.piotrskiba.angularowo.interfaces.ReportClickListener;
import pl.piotrskiba.angularowo.models.Report;
import pl.piotrskiba.angularowo.models.ReportList;

public class ReportListAdapter extends RecyclerView.Adapter<ReportListAdapter.ReportViewHolder> {

    private final Context context;
    private final ReportClickListener mClickListener;

    private ReportList reportList;

    public ReportListAdapter(Context context, ReportClickListener clickListener){
        this.context = context;
        this.mClickListener = clickListener;
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.report_list_item, parent, false);

        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Report report = reportList.getReportList().get(position);

        holder.mPlayerName.setText(report.getReported());
        holder.mReportReason.setText(context.getString(R.string.report_reason, report.getReason()));

        switch (report.getAppreciation()){
            case Constants.API_RESPONSE_REPORT_ACCEPTED:
                holder.mReportStatus.setText(context.getString(R.string.report_status_true));
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_accepted);
                break;
            case Constants.API_RESPONSE_REPORT_REJECTED:
                holder.mReportStatus.setText(context.getString(R.string.report_status_false));
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_rejected);
                break;
            case Constants.API_RESPONSE_REPORT_PENDING:
                holder.mReportStatus.setText(context.getString(R.string.report_status_awaiting));
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_pending);
                break;
            case Constants.API_RESPONSE_REPORT_UNCERTAIN:
                holder.mReportStatus.setText(context.getString(R.string.report_status_uncertain));
                holder.mReportStatusImage.setImageResource(R.drawable.ic_report_uncertain);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if(reportList != null)
            return reportList.getReportList().size();
        else
            return 0;
    }

    class ReportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_player_name)
        TextView mPlayerName;

        @BindView(R.id.tv_report_reason)
        TextView mReportReason;

        @BindView(R.id.tv_report_status)
        TextView mReportStatus;

        @BindView(R.id.iv_report_status)
        ImageView mReportStatusImage;

        ReportViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            mClickListener.onReportClick(view, reportList.getReportList().get(pos));
        }
    }

    public void setReportList(ReportList reportList){
        this.reportList = reportList;
        notifyDataSetChanged();
    }
}
