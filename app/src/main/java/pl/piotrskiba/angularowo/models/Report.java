package pl.piotrskiba.angularowo.models;

public class Report {

    private final int id;
    private final String status;
    private final String appreciation;
    private final String date;
    private final String reporter;
    private final String reported;
    private final String reason;
    private final boolean archived;

    public Report(int id, String status, String appreciation, String date, String reporter, String reported, String reason, boolean archived){
        this.id = id;
        this.status = status;
        this.appreciation = appreciation;
        this.date = date;
        this.reporter = reporter;
        this.reported = reported;
        this.reason = reason;
        this.archived = archived;
    }
}
