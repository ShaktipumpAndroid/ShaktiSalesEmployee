package bean;

import java.util.ArrayList;

import searchlist.CustomerSearch;

/**
 * Created by shakti on 11/9/2016.
 */
public class RouteDetailBean {

    static ArrayList<CustomerSearch> list_routeDetail = null;
    ArrayList<String> routemMaplist = null;

    public static ArrayList<CustomerSearch> getList_routeDetail() {
        return list_routeDetail;
    }

    public void setList_routeDetail(ArrayList<CustomerSearch> list_routeDetail) {
        this.list_routeDetail = list_routeDetail;
    }

    public ArrayList<String> getRoutemMaplist() {
        return routemMaplist;
    }

    public void setRoutemMaplist(ArrayList<String> routemMaplist) {
        this.routemMaplist = routemMaplist;
    }
}
