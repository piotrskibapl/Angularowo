package pl.piotrskiba.angularowo.interfaces;

import android.view.View;

import pl.piotrskiba.angularowo.models.Report;

public interface ReportClickListener {

    void onReportClick(View view, Report clickedReport);
}
