package pl.piotrskiba.angularowo.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReportList {

    @SerializedName("reports")
    private final List<Report> reportList;

    public ReportList(List<Report> reportList){
        this.reportList = reportList;
    }

    public List<Report> getReportList(){
        return reportList;
    }
}
