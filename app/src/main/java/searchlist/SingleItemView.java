package searchlist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

public class SingleItemView extends Activity {
    // Declare Variables
    TextView txtcustomer_number;
    TextView txtcustomer_name;
    TextView txtcustomer_category;
    TextView txtcustomer_place;
    TextView txtcustomer_distance;

    String customer_number;
    String customer_name;
    String customer_category;
    String customer_place;
    String customer_distance;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singleitemview);
        // Retrieve data from MainActivity on item click event
        Intent i = getIntent();
        // Get the results of rank
        customer_number = i.getStringExtra("customer_number");
        // Get the results of country
        customer_name = i.getStringExtra("customer_name");
        // Get the results of population
        customer_category = i.getStringExtra("customer_category");
        customer_place = i.getStringExtra("customer_place");
        customer_distance = i.getStringExtra("customer_distance");

        // Locate the TextViews in singleitemview.xml
        txtcustomer_number = (TextView) findViewById(R.id.customer_number);
        txtcustomer_name = (TextView) findViewById(R.id.customer_name);
        txtcustomer_category = (TextView) findViewById(R.id.customer_category);
        txtcustomer_place = (TextView) findViewById(R.id.customer_place);
        txtcustomer_distance = (TextView) findViewById(R.id.customer_distance);

        // Load the results into the TextViews
        txtcustomer_number.setText(customer_number);
        txtcustomer_name.setText(customer_name);
        txtcustomer_category.setText(customer_category);
        txtcustomer_place.setText(customer_place);
        txtcustomer_distance.setText(customer_distance);
    }
}