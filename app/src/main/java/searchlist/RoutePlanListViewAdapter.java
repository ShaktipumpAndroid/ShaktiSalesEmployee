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

import activity.RoutePlanDetailActivity;

public class RoutePlanListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<RoutePlanSearch> routeSearchesList = null;
    private ArrayList<RoutePlanSearch> arraylist;


    public RoutePlanListViewAdapter(Context context, List<RoutePlanSearch> routeplanSearchesList) {
        mContext = context;
        this.routeSearchesList = routeplanSearchesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<RoutePlanSearch>();
        this.arraylist.addAll(routeSearchesList);
    }

    @Override
    public int getCount() {
        return routeSearchesList.size();
    }

    @Override
    public RoutePlanSearch getItem(int position) {
        return routeSearchesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_route_plan_item, null);
            // Locate the TextViews in listview_item.xml
            // holder.customer_number = (TextView) view.findViewById(R.id.customer_number );
            holder.visit_date = (TextView) view.findViewById(R.id.visit_date);
            holder.visit_day = (TextView) view.findViewById(R.id.visit_day);
            holder.visit_route_name = (TextView) view.findViewById(R.id.visit_route);

            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        holder.visit_date.setText(routeSearchesList.get(position).getDate());
        holder.visit_day.setText(routeSearchesList.get(position).getDay());
        holder.visit_route_name.setText(routeSearchesList.get(position).getRoute_name());

//        holder.customer_distance.setText(customerSearchesList.get(position).getCountry());
//


        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                Intent intent = new Intent(mContext, RoutePlanDetailActivity.class);
                intent.putExtra("route_date", routeSearchesList.get(position).getDate());
                intent.putExtra("route_name", routeSearchesList.get(position).getRoute_name());

                mContext.startActivity(intent);


            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        routeSearchesList.clear();
        if (charText.length() == 0) {
            routeSearchesList.addAll(arraylist);
        } else {
            for (RoutePlanSearch cs : arraylist) {
                if (cs.getRoute_name().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getDate().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getDay().toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                    routeSearchesList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView customer_number;
        TextView visit_date;
        TextView visit_day;
        TextView visit_route_name;


    }

}