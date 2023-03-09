package activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.util.TextUtils;

import java.util.ArrayList;

import adapter.Controller;
import bean.BeanProduct;
import bean.LoginBean;
import bean.RouteHeaderBean;
import customlist.OrderConfirmListview;
import database.DatabaseHelper;
import model.ModelProducts;

public class TakeOrderActivity extends AppCompatActivity {
    private static final String KEY_MATNR = "matnr";
    private static final String KEY_VKORG = "vkorg";
    private static final String KEY_VTWEG = "vtweg";
    private static final String KEY_MAKTX = "maktx";
    private static final String KEY_EXTWG = "extwg";
    private static final String KEY_KBETR = "kbetr";
    private static final String KEY_MTART = "mtart";
    private static final String TOT_KBETR = "tot_kbetr";
    final Integer max = 999;
    final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
    int index;
    String str_product;
    String str_discription;
    String str_price;
    int int_price = 0;
    Controller ct;
    Menu menu;
    ArrayList<String> discription = null;
    AutoCompleteTextView autotext_discription = null;
    String discription_txt = null;
    String model_str = null, discription_str, product_str = null, price_str = "0.0", quantity_str, total_price, order_type, phone_number, customer_name, person, route_code, partner_type;
    String matnr = null, vkorg, vtweg, extwg, maktx, kbetr, mtart, tot_kbetr;
    // Database Helper
    DatabaseHelper db;
    Integer int_quantity,
            int_amount;
    private boolean isChangedStat = false;
    private Toolbar mToolbar;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    //private GoogleApiClient client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        db = new DatabaseHelper(getApplicationContext());
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        ct = (Controller) getApplicationContext();
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        discription = new ArrayList<String>();
        Button button_addtocart = (Button) findViewById(R.id.button_addtocart);
        Button button_viewcart = (Button) findViewById(R.id.button_viewcart);
        final EditText quantity = (EditText) findViewById(R.id.edittext_quantity);
        final TextView customer = (TextView) findViewById(R.id.textview_customer);
        final EditText price = (EditText) findViewById(R.id.edittext_price);
        final EditText product = (EditText) findViewById(R.id.edittext_product);
        final Spinner spinner_model = (Spinner) findViewById(R.id.spinner_model);
        autotext_discription = (AutoCompleteTextView) findViewById(R.id.discription);

        Bundle bundle = getIntent().getExtras();
        customer_name = bundle.getString("customer_name");
        phone_number = bundle.getString("phone_number");
        order_type = bundle.getString("order_type");
        route_code = bundle.getString("route_code");
        partner_type = bundle.getString("partner_type");
        if (order_type.equalsIgnoreCase("Take Order")) {
            getSupportActionBar().setTitle("Take Order");
            RouteHeaderBean.setOrder_type("7");
        }
        if (order_type.equalsIgnoreCase("Adhoc Order")) {
            getSupportActionBar().setTitle("Adhoc Order");
            RouteHeaderBean.setOrder_type("14");
        }
        person = LoginBean.getUseid();
        customer.setText(customer_name);
        ArrayList<String> model = db.getModel();
        ArrayAdapter<String> modeladapter = new ArrayAdapter<String>(this, R.layout.spinner_item, model) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            public View getDropDownView(int position, View view1, ViewGroup parent) {
                View view = super.getDropDownView(position, view1, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                tv.setGravity(Gravity.CENTER);
                return view;
            }
        };

        modeladapter.setDropDownViewResource(R.layout.spinner_layout);
        spinner_model.setPrompt("Select Model ");
        spinner_model.setAdapter(modeladapter);

        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                index = arg0.getSelectedItemPosition();
                model_str = spinner_model.getSelectedItem().toString();
                if (index != 0) {
                    autotext_discription.setEnabled(true);
                    product.setText("");
                    discription_txt = "";
                    price.setText("");
                    quantity.setText("");
                    quantity.setEnabled(false);
                    discription.clear();
                }
                //Toast.makeText(TakeOrderActivity.this, String.valueOf(index), Toast.LENGTH_LONG).show();
                // getDiscription();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

//Autocomplete text funtion

//        if(index == 0)
//        {
//            Toast.makeText(TakeOrderActivity.this, String.valueOf(index), Toast.LENGTH_LONG).show();
//        }
//        else {
//
        autotext_discription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // discription = new ArrayList<String>();
                discription.clear();
                discription_txt = "";
                product.setText("");
                price.setText("");
                quantity.setText("");
                quantity.setEnabled(false);
                discription = db.getDiscription(model_str);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(TakeOrderActivity.this, android.R.layout.simple_dropdown_item_1line, discription);
                autotext_discription.setThreshold(1);
                autotext_discription.setAdapter(adapter);
            }
        });

        autotext_discription.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                discription_txt = (String) parent.getItemAtPosition(position);
                BeanProduct beanproduct_obj = db.getBeanProduct(phone_number, discription_txt);
                matnr = beanproduct_obj.getMatnr();
                kbetr = beanproduct_obj.getKbetr();
                product.setText(matnr);
                price.setText(kbetr);
                quantity.setEnabled(true);
                // Toast.makeText(TakeOrderActivity.this, discription_txt, Toast.LENGTH_LONG).show();
            }
        });

// validation for Restrict user to select from autocompletion textview only
        autotext_discription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                discription_txt = null;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        button_addtocart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Spinner model = (Spinner) findViewById(R.id.spinner_model);
                EditText product = (EditText) findViewById(R.id.edittext_product);
                AutoCompleteTextView discription = (AutoCompleteTextView) findViewById(R.id.discription);
                EditText price = (EditText) findViewById(R.id.edittext_price);
                EditText quantity = (EditText) findViewById(R.id.edittext_quantity);

                product_str = product.getText().toString();

                if (isExist(product_str)) {
                    discription_str = discription.getText().toString();
                    price_str = price.getText().toString();
                    quantity_str = quantity.getText().toString();


                    if (submitForm()) {


                        if (!quantity.getText().toString().trim().isEmpty()) {

                            int_quantity = Integer.valueOf(quantity.getText().toString());

                            if (int_quantity <= max) {

                                if (int_quantity > 0) {

                                    Double double_price = Double.valueOf(price_str);
                                    Double double_tot_price = double_price * int_quantity;
                                    total_price = String.valueOf(double_tot_price);

                                    //   Log.d("person", "--" + phone_number + "--" + customer_name + "--" + person);
                                    ModelProducts products = new ModelProducts(model_str, discription_str, product_str, price_str, quantity_str, total_price, phone_number, customer_name, person, route_code, partner_type);
                                    ct.getCart().setProducts(products);
                                    Toast.makeText(TakeOrderActivity.this, "New CartSize:" + ct.getCart().getCartsize(), Toast.LENGTH_LONG).show();
                                    product.setText("");
                                    discription.setText("");
                                    price.setText("");
                                    quantity.setText("");

                                    quantity.setEnabled(false);

                                    updateMenu(menu);
                                } else {
                                    Toast.makeText(TakeOrderActivity.this, "Minimum Order Quantity will be 1", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(TakeOrderActivity.this, "Maximum Order Quantity will be 999", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(TakeOrderActivity.this, "Please enter quantity,it is required field", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    Toast.makeText(TakeOrderActivity.this, "Product already exist in your cart", Toast.LENGTH_LONG).show();
                }
            }

        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
       // client = new GoogleApiClient.Builder(ct).addApi(AppIndex.API).build();
    }

    public boolean submitForm() {
        boolean value;
        if ((validateModel()) &&
                (validateDiscription()) &&
                (validateProductCode()) &&
                (validatePrice()) &&

                CustomUtility.CheckGPS(this) &&
                validateDate()) {
            value = true;
        } else {
            value = false;
        }
        return value;


    }

    private boolean validateProductCode() {

        boolean value;
        if (TextUtils.isEmpty(product_str)) {
            Toast.makeText(this, "Please select Material code again", Toast.LENGTH_SHORT).show();

            value = false;
        } else {
            value = true;
        }

        return value;
    }


    private boolean validatePrice() {

        boolean value;
        if (price_str.equals("0.0")) {
            Toast.makeText(this, "Please select other material , as price not maintained.", Toast.LENGTH_SHORT).show();

            value = false;
        } else {
            value = true;
        }

        return value;
    }


    private boolean validateDiscription() {

        boolean value;
        if (TextUtils.isEmpty(discription_txt)) {
            Toast.makeText(this, "Please Enter Material", Toast.LENGTH_SHORT).show();

            value = false;
        } else {
            value = true;
        }

        return value;
    }


    private boolean validateModel() {

        boolean value;
        if (index == 0) {
            Toast.makeText(this, "Please Select Model", Toast.LENGTH_SHORT).show();

            value = false;
        } else {
            value = true;
        }

        return value;
    }


    private boolean validateDate() {
        if (CustomUtility.isDateTimeAutoUpdate(this)) {

        } else {
            CustomUtility.showSettingsAlert(this);
            return false;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        int cartcount = ct.getCart().getCartsize();

        getMenuInflater().inflate(R.menu.menu_adhoc_cart, menu);

        MenuItem item = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item, R.layout.notification_update_count_layout);
        LinearLayout notifCount = (LinearLayout) MenuItemCompat.getActionView(item);

        TextView tv = (TextView) notifCount.findViewById(R.id.count);

        tv.setText(String.valueOf(cartcount));
        this.menu = menu;

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button button_viewcart = (Button) notifCount.findViewById(R.id.button_viewcart);
        button_viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    public void updateMenu(Menu menu) {

        int cartcount = ct.getCart().getCartsize();

        MenuItem item = menu.findItem(R.id.actionbar_item);
        MenuItemCompat.setActionView(item, R.layout.notification_update_count_layout);
        LinearLayout notifCount = (LinearLayout) MenuItemCompat.getActionView(item);
        TextView tv = (TextView) notifCount.findViewById(R.id.count);
        tv.setText(String.valueOf(cartcount));

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        notifCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }
            }
        });

        Button button_viewcart = (Button) notifCount.findViewById(R.id.button_viewcart);
        button_viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onRestart() {
        super.onRestart();
        updateMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.actionbar_item:

                final Controller ct = (Controller) getApplicationContext();
                final int CartSize = ct.getCart().getCartsize();

                if (CartSize > 0) {
                    Intent in = new Intent(getBaseContext(), OrderConfirmListview.class);
                    startActivity(in);
                } else {
                    Toast.makeText(TakeOrderActivity.this, "There is no Items in Shopping Cart", Toast.LENGTH_LONG).show();
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public boolean isExist(String product1) {

        final Controller ct = (Controller) getApplicationContext();
        final int CartSize = ct.getCart().getCartsize();

        if (CartSize > 0) {
            for (int i = 0; i < CartSize; i++) {
                String productcode = ct.getCart().getProducts(i).getProduct();
                if (productcode.equals(product1)) {
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public void onBackPressed() {


        final int CartSize = ct.getCart().getCartsize();
        if (CartSize > 0) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            // Setting Dialog Title
            alertDialog.setTitle("Confirmation");
            // Setting Dialog Message
            alertDialog.setMessage("All the items will be discarded from your Cart?");
            // On pressing Settings button
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    ct.getCart().clearCart();
                    TakeOrderActivity.this.onSuperBackPressed();
                }
            });
            // on pressing cancel button
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        } else {
            TakeOrderActivity.this.onSuperBackPressed();
        }
    }

    public void onSuperBackPressed() {
        super.onBackPressed();
    }


    public void getDiscription() {


    }


}
