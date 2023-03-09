package syncdata;

import android.content.Context;
import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import database.DatabaseHelper;
import webservice.CustomHttpClient;
import webservice.WebURL;

/**
 * Created by shakti on 11/21/2016.
 */
public class SyncDataToSAP {

    Context context = null;

    public String ResetPassword(Context context, String st_login, String telnr) {
        String Status = null;

        this.context = context;

        DatabaseHelper db = new DatabaseHelper(context);
        final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

        param.add(new BasicNameValuePair("PERNR", st_login));
        param.add(new BasicNameValuePair("TELNR", telnr));


        try {
//            String obj3 = CustomHttpClient.executeHttpPost1("https://spprdsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zdistributorapp/reset_password.htm", param);
//            String obj3 = CustomHttpClient.executeHttpPost1("https://spdevsrvr1.shaktipumps.com:8423/sap/bc/bsp/sap/zmessageapp/reset_password.htm", param);

            String obj3 = CustomHttpClient.executeHttpPost1(WebURL.RESEND_PASSWORD_PAGE, param);


//            String obj3 = CustomHttpClient.executeHttpPost1("https://spprdsrvr1.shaktipumps.com:8423/sap(bD1lbiZjPTkwMA==)/bc/bsp/sap/zdistributorapp/representative.htm", param);


            if (obj3 != "") {

                Log.d("output_pass", obj3);

                JSONArray ja = new JSONArray(obj3);


                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = ja.getJSONObject(i);

                    Status = jo.getString("status");
                    break;

                }

                param.clear();

                db.close();

            } else {

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return Status;

    }

}
