package bean;

/**
 * Created by shakti on 6/6/2017.
 */
public class InprocessComplaint {
    String Key_id,
            pernr,
            cmpno,
            follow_up_date,
            reason,
            reasonid,
            cr_date,
            cr_time,
            latitude,
            longitude,
            cmpln_status,
            audio_record;

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getCmpno() {
        return cmpno;
    }

    public void setCmpno(String cmpno) {
        this.cmpno = cmpno;
    }

    public String getFollow_up_date() {
        return follow_up_date;
    }

    public void setFollow_up_date(String follow_up_date) {
        this.follow_up_date = follow_up_date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getCr_date() {
        return cr_date;
    }

    public void setCr_date(String cr_date) {
        this.cr_date = cr_date;
    }

    public String getCr_time() {
        return cr_time;
    }

    public void setCr_time(String cr_time) {
        this.cr_time = cr_time;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKey_id() {
        return Key_id;
    }

    public void setKey_id(String key_id) {
        Key_id = key_id;
    }

    public String getCmpln_status() {
        return cmpln_status;
    }

    public void setCmpln_status(String cmpln_status) {
        this.cmpln_status = cmpln_status;
    }

    public String getAudio_record() {
        return audio_record;
    }

    public void setAudio_record(String audio_record) {
        this.audio_record = audio_record;
    }

    public String getReasonid() {
        return reasonid;
    }

    public void setReasonid(String reasonid) {
        this.reasonid = reasonid;
    }
}
