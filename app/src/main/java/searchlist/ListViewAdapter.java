package searchlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.Route_customer_work_Activity;
import bean.CustomerDetailBean;

public class ListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    // public static  CustomerDetailBean customerdetailbean;
    CustomerDetailBean customerdetailbean;
    private List<CustomerSearch> customerSearchesList = null;
    private ArrayList<CustomerSearch> arraylist;

    public ListViewAdapter(Context context, List<CustomerSearch> customerSearchesList) {
        mContext = context;
        this.customerSearchesList = customerSearchesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<CustomerSearch>();
        this.arraylist.addAll(customerSearchesList);
    }

    @Override
    public int getCount() {
        return customerSearchesList.size();
    }

    @Override
    public CustomerSearch getItem(int position) {
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

        if (!customerSearchesList.get(position).getCustomer_name().isEmpty()) // add 7.1.2017
        {

            holder.customer_name.setText(customerSearchesList.get(position).getCustomer_name());
            holder.customer_category.setText(customerSearchesList.get(position).getPartner());
            holder.customer_place.setText(customerSearchesList.get(position).getDistrict_txt());
            holder.customer_distance.setText(customerSearchesList.get(position).getDistance());
        }
        //add on 1.7-2017

        holder.customer_category.setVisibility(View.VISIBLE);

        if (!customerSearchesList.get(position).getCustomer_name().isEmpty()) // add 7.1.2017
        {
            holder.customer_category.setVisibility(View.VISIBLE);
        } else {
            holder.customer_category.setVisibility(View.INVISIBLE);
            // 17.1.2017
            holder.customer_name.setText("");
            holder.customer_place.setText("");
            holder.customer_distance.setText("");
            holder.customer_category.setText("");
            // 17.1.2017
        }


        holder.customer_distance.setTextColor(Color.parseColor("#666666"));

        if (customerSearchesList.get(position).getCustomer_category().equalsIgnoreCase("NEW")) {
            holder.customer_distance.setText("New");
            holder.customer_distance.setTextColor(Color.parseColor("#0179b6"));


        }

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                //Intent intent = new Intent(mContext, SingleItemView.class);
                // Intent intent = new Intent(mContext, Route_customer_work_Activity.class);
                //                // Pass all data flag
                //  Start SingleItemView Class
                //  mContext.startActivity(intent);
//
//                // Pass all data rank
//                intent.putExtra("customer_number",(customerSearchesList.get(position).getCustomer_number()));
//                // Pass all data country
//                intent.putExtra("customer_name",(customerSearchesList.get(position).getCustomer_name()));
//                // Pass all data population
//                intent.putExtra("customer_category",(customerSearchesList.get(position).getPartner()));
//
//                intent.putExtra("route_name",(customerSearchesList.get(position).getDistrict_txt()));
//             //   intent.putExtra("customer_address",(customerSearchesList.get(position).getCustomer_place()));
//
//                intent.putExtra("customer_person",(customerSearchesList.get(position).getDistance()));
//                intent.putExtra("customer_mobile",(customerSearchesList.get(position).getDistance()));
//                intent.putExtra("customer_phone", (customerSearchesList.get(position).getDistance()));
//                intent.putExtra("customer_email", (customerSearchesList.get(position).getDistance()));
//
//
//                // Pass all data flag
//                // Start SingleItemView Class
//                mContext.startActivity(intent);


                customerdetailbean = new CustomerDetailBean();


                customerdetailbean.setKunnr(customerSearchesList.get(position).getCustomer_number());
                customerdetailbean.setPartner_name(customerSearchesList.get(position).getCustomer_name());
                customerdetailbean.setRoute_code(customerSearchesList.get(position).getRoute_code());
                customerdetailbean.setRoute_name(customerSearchesList.get(position).getRoute_name());
                customerdetailbean.setPartner_class(customerSearchesList.get(position).getPartner_class());
                customerdetailbean.setLand1(customerSearchesList.get(position).getLand1());
                customerdetailbean.setLand_txt(customerSearchesList.get(position).getLand_txt());
                customerdetailbean.setState_code(customerSearchesList.get(position).getState_code());
                customerdetailbean.setState_txt(customerSearchesList.get(position).getState_txt());
                customerdetailbean.setDistrict_code(customerSearchesList.get(position).getDistrict_code());
                customerdetailbean.setDistrict_txt(customerSearchesList.get(position).getDistrict_txt());


                customerdetailbean.setTaluka_code(customerSearchesList.get(position).getTaluka_code());
                customerdetailbean.setTaluka_txt(customerSearchesList.get(position).getTaluka_txt());
                customerdetailbean.setAddress(customerSearchesList.get(position).getAddress());
                customerdetailbean.setEmail(customerSearchesList.get(position).getEmail());
                customerdetailbean.setMob_no(customerSearchesList.get(position).getMob_no());
                customerdetailbean.setTel_number(customerSearchesList.get(position).getTel_number());
                customerdetailbean.setPincode(customerSearchesList.get(position).getPincode());
                customerdetailbean.setContact_person(customerSearchesList.get(position).getContact_person());
                customerdetailbean.setDistributor_code(customerSearchesList.get(position).getDistributor_code());
                customerdetailbean.setDistributor_name(customerSearchesList.get(position).getDistributor_name());
                customerdetailbean.setPhone_number(customerSearchesList.get(position).getPhone_number());
                customerdetailbean.setLatitude(customerSearchesList.get(position).getLatitude());
                customerdetailbean.setLongitude(customerSearchesList.get(position).getLongitude());
                customerdetailbean.setPartner(customerSearchesList.get(position).getPartner());


                if (!customerSearchesList.get(position).getCustomer_name().isEmpty()) // add 2.1.2017
                {
                    Intent intent = new Intent(mContext, Route_customer_work_Activity.class);

                    intent.putExtra("partner_name", customerSearchesList.get(position).getCustomer_name());
                    intent.putExtra("partner_latitude", customerSearchesList.get(position).getLatitude());
                    intent.putExtra("partner_longitude", customerSearchesList.get(position).getLongitude());
                    mContext.startActivity(intent);

                }

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
            for (CustomerSearch cs : arraylist) {
                if (cs.getCustomer_name().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getPartner().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getDistrict_txt().toLowerCase(Locale.getDefault()).contains(charText)) {
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