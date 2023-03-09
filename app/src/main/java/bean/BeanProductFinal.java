package bean;

/**
 * Created by Administrator on 10/17/2016.
 */
public class BeanProductFinal {
    String matnr, extwg, maktx, kbetr, menge, tot_kbetr, phone_number, customer_name, person, cr_date, cr_time, latitude, longitude, route_code, partner_type, key_id;

    public BeanProductFinal() {
    }

    public BeanProductFinal(String phone_number,
                            String matnr,
                            String extwg,
                            String maktx,
                            String kbetr,
                            String menge,
                            String tot_kbetr,
                            String customer_name,
                            String person,
                            String cr_date,
                            String cr_time,
                            String latitude,
                            String longitude,
                            String route_code,
                            String partner_type

    ) {
        this.phone_number = phone_number;
        this.matnr = matnr;
        this.extwg = extwg;
        this.maktx = maktx;
        this.kbetr = kbetr;
        this.menge = menge;
        this.tot_kbetr = tot_kbetr;
        this.customer_name = customer_name;
        this.person = person;
        this.cr_date = cr_date;
        this.cr_time = cr_time;

        this.latitude = latitude;
        this.longitude = longitude;

        this.route_code = route_code;
        this.partner_type = partner_type;


    }


    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getExtwg() {
        return extwg;
    }

    public void setExtwg(String extwg) {
        this.extwg = extwg;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getKbetr() {
        return kbetr;
    }

    public void setKbetr(String kbetr) {
        this.kbetr = kbetr;
    }

    public String getMenge() {
        return menge;
    }

    public void setMenge(String menge) {
        this.menge = menge;
    }

    public String getTot_kbetr() {
        return tot_kbetr;
    }

    public void setTot_kbetr(String tot_kbetr) {
        this.tot_kbetr = tot_kbetr;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
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


    public String getRoute_code() {
        return route_code;
    }

    public void setRoute_code(String route_code) {
        this.route_code = route_code;
    }

    public String getPartner_type() {
        return partner_type;
    }

    public void setPartner_type(String partner_type) {
        this.partner_type = partner_type;
    }


    public String getKey_id() {
        return key_id;
    }

    public void setKey_id(String key_id) {
        this.key_id = key_id;
    }


}
