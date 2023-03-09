package searchlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UnSyncDataListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<UnsyncData> unSyncdataSearchesList = null;
    private ArrayList<UnsyncData> arraylist;


    public UnSyncDataListViewAdapter(Context context, List<UnsyncData> unsyncdataSearchesList) {
        mContext = context;
        this.unSyncdataSearchesList = unsyncdataSearchesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<UnsyncData>();
        this.arraylist.addAll(unSyncdataSearchesList);
    }

    @Override
    public int getCount() {
        return unSyncdataSearchesList.size();
    }

    @Override
    public UnsyncData getItem(int position) {
        return unSyncdataSearchesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_unsync_item, null);
            // Locate the TextViews in listview_item.xml
            // holder.customer_number = (TextView) view.findViewById(R.id.customer_number );
            holder.unsync_name = (TextView) view.findViewById(R.id.unsync_name);
            holder.unsync_value = (TextView) view.findViewById(R.id.unsync_value);


            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        holder.unsync_name.setText(unSyncdataSearchesList.get(position).getActivity_name());
        holder.unsync_value.setText(unSyncdataSearchesList.get(position).getValue());


//
//        // Listen for ListView Item Click
//        view.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//
//
//                Intent intent = new Intent(mContext, RoutePlanDetailActivity.class);
//                intent.putExtra("route_date",routeSearchesList.get(position).getDate());
//                intent.putExtra("route_name",routeSearchesList.get(position).getRoute_name());
//
//                mContext.startActivity(intent);
//
//
//
//
//
//            }
//        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        unSyncdataSearchesList.clear();
        if (charText.length() == 0) {
            unSyncdataSearchesList.addAll(arraylist);
        } else {
            for (UnsyncData cs : arraylist) {
                if (cs.getActivity_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    unSyncdataSearchesList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {

        TextView unsync_name;
        TextView unsync_value;


    }

}