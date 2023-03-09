
package adapter;

import android.content.Context;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;

import model.ModelCart;
import model.ModelProducts;

@ReportsCrashes(mailTo = "dinesh.yadav@shaktipumps.com;tl.cc@shaktipumps.com;shailesh.b@asapinfotech.com",

        customReportContent = { ReportField.USER_APP_START_DATE,
                ReportField.USER_CRASH_DATE,
                ReportField.APP_VERSION_CODE,
                ReportField.APP_VERSION_NAME,
                ReportField.ANDROID_VERSION,
                ReportField.DEVICE_ID,
                ReportField.BUILD,
                ReportField.BRAND,
                ReportField.DEVICE_FEATURES,
                ReportField.PHONE_MODEL,
                ReportField.CUSTOM_DATA,
                ReportField.PACKAGE_NAME,
                ReportField.REPORT_ID,
                ReportField.STACK_TRACE,
                ReportField.LOGCAT},
        mode = ReportingInteractionMode.TOAST,
        resToastText = R.string.crash_toast_text) //you get to define resToastText


public class Controller extends MultiDexApplication {

    private final ArrayList<ModelProducts> myproducts = new ArrayList<ModelProducts>();
    private final ModelCart myCart = new ModelCart();

    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(this);
        super.attachBaseContext(base);
        ACRA.init(this);

    }

    public ModelProducts getProducts(final int pPosition) {
        return myproducts.get(pPosition);
    }

    public void setProducts(ModelProducts products) {
        myproducts.add(products);
    }

    public ModelCart getCart() {
        return myCart;
    }

    public int getProductArraylistsize() {
        return myproducts.size();
    }

}


