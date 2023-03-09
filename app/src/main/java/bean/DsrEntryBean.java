package bean;

/**
 * Created by shakti on 12/12/2016.
 */
public class DsrEntryBean {
    String pernr,
            date,
            time,
            help_name,
            dsr_agenda,
            dsr_outcomes,
            latitude,
            longitude,
            key_id;

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDsr_agenda() {
        return dsr_agenda;
    }

    public void setDsr_agenda(String dsr_agenda) {
        this.dsr_agenda = dsr_agenda;
    }

    public String getDsr_outcomes() {
        return dsr_outcomes;
    }

    public void setDsr_outcomes(String dsr_outcomes) {
        this.dsr_outcomes = dsr_outcomes;
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

    public String getHelp_name() {
        return help_name;
    }

    public void setHelp_name(String help_name) {
        this.help_name = help_name;
    }

    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }
}
