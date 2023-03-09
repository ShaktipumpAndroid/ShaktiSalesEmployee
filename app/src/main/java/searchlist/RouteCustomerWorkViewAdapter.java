package searchlist;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;

import activity.Capture_employee_gps_location;
import activity.CustomUtility;
import bean.CustomerDetailBean;

public class RouteCustomerWorkViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    RouteCustomerWorkView routeCustomerWorkView;
    CustomerDetailBean customerdetailbean = null;
    private List<RouteCustomerWorkView> routeCustomerWorkViews = null;
    private ArrayList<RouteCustomerWorkView> arraylist;

    public RouteCustomerWorkViewAdapter(Context context, List<RouteCustomerWorkView> routeCustomerWorkViews) {
        mContext = context;
        this.routeCustomerWorkViews = routeCustomerWorkViews;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<RouteCustomerWorkView>();
        this.arraylist.addAll(routeCustomerWorkViews);
    }

    @Override
    public int getCount() {
        return routeCustomerWorkViews.size();
    }

    @Override
    public RouteCustomerWorkView getItem(int position) {
        return routeCustomerWorkViews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.route_customer_work_view_item, null);
            // Locate the TextViews in listview_item.xml
            // holder.customer_number = (TextView) view.findViewById(R.id.customer_number );
            holder.field = (TextView) view.findViewById(R.id.test_field);
            holder.image = (TextView) view.findViewById(R.id.test_image);
            holder.value = (TextView) view.findViewById(R.id.test_value);

            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }


        holder.image.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


        //  holder.customer_number.setText(customerSearchesList.get(position).getCustomer_number());
        holder.field.setText(routeCustomerWorkViews.get(position).getField());

        holder.value.setText(routeCustomerWorkViews.get(position).getValue());

        //holder.image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.phone_call, 0);
        if (routeCustomerWorkViews.get(position).getField().equalsIgnoreCase("Mobile No :")) {
            holder.image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.phone_call, 0);
        }

        if (routeCustomerWorkViews.get(position).getField().equalsIgnoreCase("Contact Ph:")) {
            holder.image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.phone_call, 0);
        }

        if (routeCustomerWorkViews.get(position).getField().equalsIgnoreCase("Email :")) {
            holder.image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.mail, 0);
        }


        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                if (routeCustomerWorkViews.get(position).getField().equalsIgnoreCase("Mobile No :")) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(customerdetailbean.getPhone_number());
                        }

                    } else {
                        Make_Call(customerdetailbean.getPhone_number());
                    }
                }


                if (routeCustomerWorkViews.get(position).getField().equalsIgnoreCase("Contact Ph:")) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(customerdetailbean.getPhone_number());
                        }

                    } else {
                        Make_Call(customerdetailbean.getPhone_number());
                    }
                }

                if (routeCustomerWorkViews.get(position).getField().equalsIgnoreCase("Email :")) {
                    if (!customerdetailbean.getEmail().equals("") && !customerdetailbean.getEmail().equals(null)) {

                        new Capture_employee_gps_location(mContext, "13", customerdetailbean.getPhone_number());


                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_EMAIL, new String[]{customerdetailbean.getEmail()});
                        // i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
                        // i.putExtra(Intent.EXTRA_TEXT   , "body of email");
                        try {
                            mContext.startActivity(Intent.createChooser(i, "Send mail..."));
                        } catch (android.content.ActivityNotFoundException ex) {
                            CustomUtility.ShowToast("There are no email clients installed.", mContext);
                        }

                    }
                }


            }
        });

        return view;
    }

    private void Make_Call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            new Capture_employee_gps_location(mContext, "12", mobile);
            intent.setData(Uri.parse("tel:" + mobile));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder {
        TextView field;
        TextView value;
        TextView image;


    }

}