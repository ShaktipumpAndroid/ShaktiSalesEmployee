package customlist;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

import activity.CustomUtility;
import activity.GPSTracker;
import adapter.Controller;
import backgroundservice.SyncDataService;
import bean.BeanProductFinal;
import database.DatabaseHelper;
import model.ModelProducts;

/**
 * Created by Administrator on 10/25/2016.
 */
public class OrderConfirmListview extends AppCompatActivity {
    public static TextView grand_total_txt;
    Controller ct;
    DatabaseHelper db;
    CustomUtility customutility = null;
    double double_grandtotal = 0;
    double double_total = 0;
    Menu menu;
    Context context;
    double latitude;
    double longitude;
    Button button_place_order;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(OrderConfirmListview.this, mString, Toast.LENGTH_LONG).show();
        }
    };
    private Toolbar mToolbar;


    public static void changeGrandTotal(String total) {
        grand_total_txt.setText("" + total);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        customutility = new CustomUtility();

        setContentView(R.layout.activity_customlistview);
        db = new DatabaseHelper(this);

        ct = (Controller) getApplicationContext();
        final int CartSize = ct.getCart().getCartsize();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        button_place_order = (Button) findViewById(R.id.button_place_order);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Adhoc cart");

        GPSTracker gps = new GPSTracker(context);
        latitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLatitude()));
        longitude = Double.parseDouble(new DecimalFormat("##.#####").format(gps.getLongitude()));

        // Construct the data source
        ArrayList<ModelProducts> arraylist = new ArrayList<ModelProducts>();


        if (CartSize > 0) {
            double_grandtotal = 0;
            for (int i = 0; i < CartSize; i++) {

                String product = ct.getCart().getProducts(i).getProduct();
                String price = ct.getCart().getProducts(i).getPrice();
                String discription = ct.getCart().getProducts(i).getDiscription();
                String model = ct.getCart().getProducts(i).getModel();
                String quantity = ct.getCart().getProducts(i).getQuantity();
                String total_price = ct.getCart().getProducts(i).getTotal_price();
                String phone_number = ct.getCart().getProducts(i).getPhone_number();
                String customer_name = ct.getCart().getProducts(i).getCustomer_name();
                String person = ct.getCart().getProducts(i).getPerson();

                String route_code = ct.getCart().getProducts(i).getRoute_code();
                String partner_type = ct.getCart().getProducts(i).getPartner_type();


                double_total = Double.valueOf(total_price);
                double_grandtotal = double_grandtotal + double_total;
                ModelProducts modelProducts = new ModelProducts(model, discription, product, price, quantity, total_price, phone_number, customer_name, person, route_code, partner_type);
                arraylist.add(modelProducts);
            }

            // Create the adapter to convert the array to views
            SingleProductList adapter = new SingleProductList(OrderConfirmListview.this, arraylist);
            adapter.notifyDataSetChanged();

            // Attach the adapter to a ListView
            ListView listview = (ListView) findViewById(R.id.list);
            listview.setAdapter(adapter);
        } else {
            Toast.makeText(OrderConfirmListview.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
        }


        button_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Data Save alert !");
                alertDialog.setMessage("Do you want to save data ?");


                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        final int CartSize = ct.getCart().getCartsize();
                        String show = "";
                        if (CartSize > 0) {
                            for (int i = 0; i < CartSize; i++) {
                                String matnr = ct.getCart().getProducts(i).getProduct();
                                String kbetr = ct.getCart().getProducts(i).getPrice();
                                String maktx = ct.getCart().getProducts(i).getDiscription();
                                String extwg = ct.getCart().getProducts(i).getModel();
                                String tot_kbetr = ct.getCart().getProducts(i).getTotal_price();
                                String menge = ct.getCart().getProducts(i).getQuantity();
                                String phone_number = ct.getCart().getProducts(i).getPhone_number();
                                String customer_name = ct.getCart().getProducts(i).getCustomer_name();
                                String person = ct.getCart().getProducts(i).getPerson();
                                String cr_date = customutility.getCurrentDate();
                                String cr_time = customutility.getCurrentTime();


                                String route_code = ct.getCart().getProducts(i).getRoute_code();
                                String partner_type = ct.getCart().getProducts(i).getPartner_type();


                                Log.d("person1", "--" + phone_number + "--" + customer_name + "--" + person);

                                BeanProductFinal beanProductFinal = new BeanProductFinal(phone_number,
                                        matnr,
                                        extwg,
                                        maktx,
                                        kbetr,
                                        menge,
                                        tot_kbetr,
                                        customer_name,
                                        person,
                                        cr_date,
                                        cr_time,
                                        String.valueOf(latitude),
                                        String.valueOf(longitude),
                                        route_code,
                                        partner_type
                                );
                                db.createProductFinal(beanProductFinal);

                                // take order
                                // 7 take order"

                                //  new Capture_employee_gps_location(OrderConfirmListview.this,RouteHeaderBean.getOrder_type(),phone_number);

//                        // adhoc order
//                        new Capture_employee_gps_location(OrderConfirmListview.this,"14");


                            }
//              Clear cart
                            ct.getCart().clearCart();
//              Sync data to SAP
                            // new SyncDataToSAP().SyncOrderSap(context);

//                    Delete data from SQLLite database table
                            //    db.deleteAdhocOrderFinal();


                            Toast.makeText(context, "Order saved successfully", Toast.LENGTH_LONG).show();
                            SyncTakeOrderInBackground();

                            //  Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();

                        }

                    }
                });


                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });
//

    }

    public void SyncTakeOrderInBackground() {


        Intent i = new Intent(OrderConfirmListview.this, SyncDataService.class);
        startService(i);


//        progressDialog = ProgressDialog.show(OrderConfirmListview.this, "", "Connecting to internet..");
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                if (CustomUtility.isOnline(OrderConfirmListview.this))
//                {
//
//                    Intent i = new Intent(OrderConfirmListview.this, SyncDataService.class);
//                     i.putExtra("sync_data", "sync_take_order");
//                     startService(i);
//
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//
//                    Message msg = new Message();
//                    msg.obj = "Order Sync Successfully";
//                    mHandler.sendMessage(msg);
//
//
//                } else {
//                      if ((progressDialog != null) && progressDialog.isShowing()) {

//                    Message msg = new Message();
//                    msg.obj = "No internet Connection . Order saved in offline";
//                    mHandler.sendMessage(msg);
//
//                }
//
//            }
//        }).start();


    }

   /* public void salveData() {

        button_place_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int CartSize = ct.getCart().getCartsize();
                String show = "";
                if (CartSize > 0) {
                    for (int i = 0; i < CartSize; i++) {
                        String matnr = ct.getCart().getProducts(i).getProduct();
                        String kbetr = ct.getCart().getProducts(i).getPrice();
                        String maktx = ct.getCart().getProducts(i).getDiscription();
                        String extwg = ct.getCart().getProducts(i).getModel();
                        String tot_kbetr = ct.getCart().getProducts(i).getTotal_price();
                        String menge = ct.getCart().getProducts(i).getQuantity();
                        String phone_number = ct.getCart().getProducts(i).getPhone_number();
                        String customer_name = ct.getCart().getProducts(i).getCustomer_name();
                        String person = ct.getCart().getProducts(i).getPerson();
                        String cr_date = customutility.getCurrentDate();
                        String cr_time = customutility.getCurrentTime();
                        String route_code = ct.getCart().getProducts(i).getRoute_code();
                        String partner_type = ct.getCart().getProducts(i).getPartner_type();


                        Log.d("person1", "--" + phone_number + "--" + customer_name + "--" + person);

                        BeanProductFinal beanProductFinal = new BeanProductFinal(phone_number,
                                matnr,
                                extwg,
                                maktx,
                                kbetr,
                                menge,
                                tot_kbetr,
                                customer_name,
                                person,
                                cr_date,
                                cr_time,
                                String.valueOf(latitude),
                                String.valueOf(longitude),
                                route_code,
                                partner_type
                        );
                        db.createProductFinal(beanProductFinal);


                    }
//              Clear cart
                    ct.getCart().clearCart();
//              Sync data to SAP
                    new SyncDataToSAP().SyncOrderSap(context);
//                    Delete data from SQLLite database table
                    //    db.deleteAdhocOrderFinal();
                    Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();

                }
            }
        });


    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_customlist, menu);
        MenuItem item = menu.findItem(R.id.grand_total);
        MenuItemCompat.setActionView(item, R.layout.notification_update_grandtotal_layout);
        LinearLayout notifCount = (LinearLayout) MenuItemCompat.getActionView(item);
        grand_total_txt = (TextView) notifCount.findViewById(R.id.grand_total_txt);
        grand_total_txt.setText("" + double_grandtotal);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
