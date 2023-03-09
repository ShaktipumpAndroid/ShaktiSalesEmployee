
package bean.vkbean;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class RemarkResponseList {

    @SerializedName("action")
    private String mAction;
    @SerializedName("aedtm")
    private String mAedtm;
    @SerializedName("cmp_pen_re")
    private String mCmpPenRe;
    @SerializedName("cmpno")
    private String mCmpno;
    @SerializedName("counter")
    private String mCounter;
    @SerializedName("ename")
    private String mEname;
    @SerializedName("pernr")
    private String mPernr;
    @SerializedName("srv_kunnr")
    private String mSrvKunnr;
    @SerializedName("status")
    private String mStatus;

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public String getAedtm() {
        return mAedtm;
    }

    public void setAedtm(String aedtm) {
        mAedtm = aedtm;
    }

    public String getCmpPenRe() {
        return mCmpPenRe;
    }

    public void setCmpPenRe(String cmpPenRe) {
        mCmpPenRe = cmpPenRe;
    }

    public String getCmpno() {
        return mCmpno;
    }

    public void setCmpno(String cmpno) {
        mCmpno = cmpno;
    }

    public String getCounter() {
        return mCounter;
    }

    public void setCounter(String counter) {
        mCounter = counter;
    }

    public String getEname() {
        return mEname;
    }

    public void setEname(String ename) {
        mEname = ename;
    }

    public String getPernr() {
        return mPernr;
    }

    public void setPernr(String pernr) {
        mPernr = pernr;
    }

    public String getSrvKunnr() {
        return mSrvKunnr;
    }

    public void setSrvKunnr(String srvKunnr) {
        mSrvKunnr = srvKunnr;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
