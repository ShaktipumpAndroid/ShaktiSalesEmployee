package customlist;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.shaktipumps.shakti.shaktisalesemployee.R;
import java.util.ArrayList;

/**
 * Created by shakti on 11/8/2016.
 */
public class RouteCustomList extends ArrayAdapter<String> {

    private final Activity context;
    ArrayList<String> arrayList, arrayList1;

    public RouteCustomList(Activity context,
                           ArrayList<String> arrayList, ArrayList<String> arrayList1) {
        super(context, R.layout.custom_route_list, arrayList);
        this.context = context;
        this.arrayList = arrayList;
        this.arrayList1 = arrayList1;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.custom_route_list, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.route_name);
        txtTitle.setText(arrayList.get(position));
        TextView txtTitle1 = (TextView) rowView.findViewById(R.id.route_status);
        txtTitle1.setText(arrayList1.get(position));
        return rowView;
    }
}