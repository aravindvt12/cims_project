import java.sql.Timestamp;

public class Incident {
    private int incidentId;
    private String title;
    private String description;
    private String severity;
    private String status;
    private String reportedBy;
    private Integer analystId;
    private String analystName;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Incident(int incidentId, String title, String description,
                    String severity, String status, String reportedBy,
                    Integer analystId, String analystName,
                    Timestamp createdAt, Timestamp updatedAt) {
        this.incidentId = incidentId;
        this.title = title;
        this.description = description;
        this.severity = severity;
        this.status = status;
        this.reportedBy = reportedBy;
        this.analystId = analystId;
        this.analystName = analystName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getIncidentId() { return incidentId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getSeverity() { return severity; }
    public String getStatus() { return status; }
    public String getReportedBy() { return reportedBy; }
    public Integer getAnalystId() { return analystId; }
    public String getAnalystName() { return analystName; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }

    public void display() {
        System.out.println("----------------------------------------");
        System.out.println("Incident ID : " + incidentId);
        System.out.println("Title       : " + title);
        System.out.println("Description : " + description);
        System.out.println("Severity    : " + severity);
        System.out.println("Status      : " + status);
        System.out.println("Reported by : " + reportedBy);
        System.out.println("Analyst     : " + (analystName == null ? "Unassigned" : analystName));
        System.out.println("Created at  : " + createdAt);
        System.out.println("Updated at  : " + updatedAt);
    }
}
