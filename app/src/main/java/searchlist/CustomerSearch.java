package searchlist;

/**
 * Created by shakti on 11/10/2016.
 */
public class CustomerSearch {
    private String customer_number;
    private String customer_name;
    //private String customer_category;
    //private String customer_place;
    private String customer_distance;

    private String route_code;
    private String route_name;

    private String partner;
    private String partner_class;
    private String partner_name;
    private String land1;
    private String land_txt;
    private String state_code;
    private String state_txt;
    private String district_code;
    private String district_txt;
    private String taluka_code;
    private String taluka_txt;
    private String address;
    private String email;
    private String mob_no;
    private String tel_number;
    private String pincode;
    private String contact_person;
    private String distributor_code;
    private String distributor_name;
    private String phone_number;
    private String latitude;
    private String longitude;
    private String vkorg;
    private String vtweg;
    private String customer_category;
    private String ktokd;


    public CustomerSearch(String customer_number,
                          String customer_name,
                          String partner,
                          String district_code,
                          String district_txt,
                          String customer_distance,
                          String route_code,
                          String route_name,
                          String partner_class,
                          String land1,
                          String land_txt,
                          String state_code,
                          String state_txt,
                          String taluka_code,
                          String taluka_txt,
                          String address,
                          String email,
                          String mob_no,
                          String tel_number,
                          String pincode,
                          String contact_person,
                          String distributor_code,
                          String distributor_name,
                          String phone_number,
                          String latitude,
                          String longitude,
                          String vkorg,
                          String vtweg,
                          String customer_category,
                          String ktokd

    ) {
        this.customer_number = customer_number;
        this.customer_name = customer_name;
        this.partner = partner;
        this.customer_distance = customer_distance;
        this.district_code = district_code;
        this.district_txt = district_txt;
        this.route_code = route_code;
        this.route_name = route_name;
        this.partner_class = partner_class;
        this.land1 = land1;
        this.land_txt = land_txt;
        this.state_code = state_code;
        this.state_txt = state_txt;
        this.taluka_code = taluka_code;
        this.taluka_txt = taluka_txt;
        this.address = address;
        this.email = email;
        this.mob_no = mob_no;
        this.tel_number = tel_number;
        this.pincode = pincode;
        this.contact_person = contact_person;
        this.distributor_code = distributor_code;
        this.distributor_name = distributor_name;
        this.phone_number = phone_number;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vkorg = vkorg;
        this.vtweg = vtweg;
        this.customer_category = customer_category;
        this.ktokd = ktokd;


    }

    public String getCustomer_number() {
        return customer_number;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getPartner() {
        return partner;
    }

    public String getDistrict_code() {
        return district_code;
    }

    public String getDistrict_txt() {
        return district_txt;
    }

    public String getDistance() {
        return customer_distance;
    }

    public String getRoute_code() {
        return route_code;
    }

    public String getRoute_name() {
        return route_name;
    }


    public String getPartner_class() {
        return partner_class;
    }

    public String getLand1() {
        return land1;
    }


    public String getLand_txt() {
        return land_txt;
    }

    public String getState_code() {
        return state_code;
    }

    public String getState_txt() {
        return state_txt;
    }

    public String getTaluka_code() {
        return taluka_code;
    }

    public String getTaluka_txt() {
        return taluka_txt;
    }


    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getMob_no() {
        return mob_no;
    }

    public String getTel_number() {
        return tel_number;
    }

    public String getPincode() {
        return pincode;
    }

    public String getContact_person() {
        return contact_person;
    }


    public String getDistributor_code() {
        return distributor_code;
    }


    public String getDistributor_name() {
        return distributor_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getCustomer_distance() {
        return customer_distance;
    }

    public String getPartner_name() {
        return partner_name;
    }


    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getCustomer_category() {
        return customer_category;
    }

    public void setCustomer_category(String customer_category) {
        this.customer_category = customer_category;
    }

    public String getKtokd() {
        return ktokd;
    }

    public void setKtokd(String ktokd) {
        this.ktokd = ktokd;
    }
}
