package model;

/**
 * Created by Administrator on 10/25/2016.
 */
public class ModelProducts {

    private String model;
    private String discription;
    private String product;
    private String price;
    private String quantity;
    private String total_price;
    private String phone_number;
    private String customer_name;
    private String person;
    private String route_code;
    private String partner_type;


    public ModelProducts() {
    }

    public ModelProducts(String model, String discription, String product, String price, String quantity, String total_price, String phone_number, String customer_name, String person, String route_code, String partner_type) {
        this.model = model;
        this.discription = discription;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
        this.total_price = total_price;
        this.phone_number = phone_number;
        this.customer_name = customer_name;
        this.person = person;
        this.route_code = route_code;
        this.partner_type = partner_type;

    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
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
}

