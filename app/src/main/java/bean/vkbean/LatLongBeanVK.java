
package bean.vkbean;



import java.util.List;

@SuppressWarnings("unused")
public class LatLongBeanVK {


    private String mLat;

    private String mKunnr;

    private String mLong;

    private List<LatLongBeanVK> mResponse;

    public String getLat() {
        return mLat;
    }

    public void setLat(String lat) {
        mLat = lat;
    }

    public String getKunnr() {
        return mKunnr;
    }

    public void setKunnr(String kunnr) {
        mKunnr = kunnr;
    }



    public String getLong() {
        return mLong;
    }

    public void setLong(String longg) {
        mLong = longg;
    }

}
