package searchlist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.TakeOrderActivity;
import bean.CustomerDetailBean;

public class AdhocOrderListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    // public static  CustomerDetailBean customerdetailbean;
    CustomerDetailBean customerdetailbean;
    private List<CustomerSearchAdhocOrder> customerSearchesList = null;
    private ArrayList<CustomerSearchAdhocOrder> arraylist;

    public AdhocOrderListViewAdapter(Context context, List<CustomerSearchAdhocOrder> customerSearchesList) {
        mContext = context;
        this.customerSearchesList = customerSearchesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CustomerSearchAdhocOrder>();
        this.arraylist.addAll(customerSearchesList);
    }

    @Override
    public int getCount() {
        return customerSearchesList.size();
    }

    @Override
    public CustomerSearchAdhocOrder getItem(int position) {
        return customerSearchesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_item, null);
            // Locate the TextViews in listview_item.xml
            // holder.customer_number = (TextView) view.findViewById(R.id.customer_number );
            holder.customer_name = (TextView) view.findViewById(R.id.customer_name);
            holder.customer_category = (TextView) view.findViewById(R.id.customer_category);
            holder.customer_place = (TextView) view.findViewById(R.id.customer_place);
            holder.customer_distance = (TextView) view.findViewById(R.id.customer_distance);
            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        //  holder.customer_number.setText(customerSearchesList.get(position).getCustomer_number());
        holder.customer_name.setText(customerSearchesList.get(position).getPartner_name());
        holder.customer_category.setText(customerSearchesList.get(position).getPartner_type());
        holder.customer_place.setText(customerSearchesList.get(position).getDistrict());
        holder.customer_distance.setText(customerSearchesList.get(position).getCountry());


        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Intent intent = new Intent(mContext, TakeOrderActivity.class);
                intent.putExtra("customer_name", customerSearchesList.get(position).getPartner_name());
                intent.putExtra("phone_number", customerSearchesList.get(position).getPhone_number());
                intent.putExtra("order_type", "Adhoc Order");

                intent.putExtra("partner_type", customerSearchesList.get(position).getPartner_type());
                intent.putExtra("route_code", "0000000000");

                mContext.startActivity(intent);


//                Intent intent = new Intent(mContext, Route_customer_work_Activity.class);
//                intent.putExtra("partner_name", customerSearchesList.get(position).getCustomer_name());
//                intent.putExtra("partner_latitude",customerSearchesList.get(position).getLatitude());
//                intent.putExtra("partner_longitude", customerSearchesList.get(position).getLongitude());
//                //
//
//                //  Start SingleItemView Class
//                mContext.startActivity(intent);


            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        customerSearchesList.clear();
        if (charText.length() == 0) {
            customerSearchesList.addAll(arraylist);
        } else {
            for (CustomerSearchAdhocOrder cs : arraylist) {
                if (cs.getPartner_name().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getDistrict().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getCountry().toLowerCase(Locale.getDefault()).contains(charText)

                ) {
                    customerSearchesList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView customer_number;
        TextView customer_name;
        TextView customer_category;
        TextView customer_place;
        TextView customer_distance;

    }

}