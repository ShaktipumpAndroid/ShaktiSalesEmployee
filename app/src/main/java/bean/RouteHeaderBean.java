package bean;

/**
 * Created by shakti on 12/20/2016.
 */
public class RouteHeaderBean {
    public static String route_code,
            route_name,
            vkorg,
            vtweg,
            order_type,
            ktokd;


    public static String getRoute_name() {
        return route_name;
    }

    public static void setRoute_name(String route_name0) {
        route_name = route_name0;
    }

    public static String getRoute_code() {
        return route_code;
    }

    public static void setRoute_code(String route_code0) {
        route_code = route_code0;
    }

    public static String getVkorg() {
        return vkorg;
    }

    public static void setVkorg(String vkorg0) {
        vkorg = vkorg0;
    }

    public static String getVtweg() {
        return vtweg;
    }

    public static void setVtweg(String vtweg0) {
        vtweg = vtweg0;
    }

    public static String getOrder_type() {
        return order_type;
    }

    public static void setOrder_type(String order_type) {
        RouteHeaderBean.order_type = order_type;
    }

    public static String getKtokd() {
        return ktokd;
    }

    public static void setKtokd(String ktokd) {
        RouteHeaderBean.ktokd = ktokd;
    }
}
