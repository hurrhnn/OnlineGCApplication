package app.mobilecontests.onlinegcapplication.dashboard;

public class DashboardProperty {

    private String number;
    private String startDate;
    private String endDate;
    private String title;
    private String subject;
    private String progressBar;
    private int progressPercent;
    private String progressPercentString;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(String progressBar) {
        this.progressBar = progressBar;
    }

    public int getProgressPercent() {
        return progressPercent;
    }

    public void setProgressPercent(int progressPercent) {
        this.progressPercent = progressPercent;
    }

    public String getProgressPercentString() {
        return progressPercentString;
    }

    public void setProgressPercentString(String progressPercentString) {
        this.progressPercentString = progressPercentString;
    }

    public DashboardProperty(String number, String startDate, String endDate, String title, String subject, String progressBar, int progressPercent, String progressPercentString) {
        this.number = number;
        this.startDate = startDate;
        this.endDate = endDate;
        this.title = title;
        this.subject = subject;
        this.progressBar = progressBar;
        this.progressPercent = progressPercent;
        this.progressPercentString = progressPercentString;
    }
}
