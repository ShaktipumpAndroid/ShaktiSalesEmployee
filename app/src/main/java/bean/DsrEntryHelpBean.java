package bean;

/**
 * Created by shakti on 11/5/2016.
 */
public class DsrEntryHelpBean {
    String btrtl, actcode, actdtl;

    public DsrEntryHelpBean(String btrtl, String actcode, String actdtl) {
        this.btrtl = btrtl;
        this.actcode = actcode;
        this.actdtl = actdtl;
    }

    public DsrEntryHelpBean() {
    }

    public String getBtrtl() {
        return btrtl;
    }

    public void setBtrtl(String btrtl) {
        this.btrtl = btrtl;
    }

    public String getActcode() {
        return actcode;
    }

    public void setActcode(String actcode) {
        this.actcode = actcode;
    }

    public String getActdtl() {
        return actdtl;
    }

    public void setActdtl(String actdtl) {
        this.actdtl = actdtl;
    }
}
