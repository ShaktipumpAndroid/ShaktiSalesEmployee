package searchlist.complaint;

/**
 * Created by shakti on 6/9/2017.
 */
public class SearchComplaintAction {

    String pernr,
            ename,
            follow_up_date,
            date,
            pending_reason,
            reason,
            status;

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getPending_reason() {
        return pending_reason;
    }

    public void setPending_reason(String pending_reason) {
        this.pending_reason = pending_reason;
    }

    public String getFollow_up_date() {
        return follow_up_date;
    }

    public void setFollow_up_date(String follow_up_date) {
        this.follow_up_date = follow_up_date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
