package bean;

/**
 * Created by shakti on 10/7/2016.
 */

public class LoginBean {

    public static String userid;
    public static String username;


    public static void setLogin(String id, String name) {
        userid = id;

        username = name;
    }


    public static String getUsername() {
        return username;
    }

    public static String getUseid() {
        return userid;
    }


}
