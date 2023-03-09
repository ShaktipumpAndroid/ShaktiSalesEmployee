package adapter;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by shakti on 10/27/2016.
 */


public class ConnectivityReceiver extends AppCompatActivity {
    ConnectivityManager cn;

    public boolean getConnectivity(Context context) {
        boolean value;

        cn = (ConnectivityManager) getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo nf = cn.getActiveNetworkInfo();
        if (nf != null && nf.isConnected() == true) {
            value = true;

            // Toast.makeText(this, "Network Available", Toast.LENGTH_LONG).show();

        } else {
            value = false;
            //  Toast.makeText(this, "Network Not Available", Toast.LENGTH_LONG).show();

        }


        return value;
    }


}