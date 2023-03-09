package searchlist.complaint;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;

import database.DatabaseHelper;


/**
 * Created by shakti on 6/9/2017.
 */

public class SearchComplaintActionListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    String str_cmp_no = "",pending_category_name="";
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<SearchComplaintAction> complaintSearchlist = null;
    private ArrayList<SearchComplaintAction> arraylist;


    public SearchComplaintActionListViewAdapter(Context context, List<SearchComplaintAction> complaintActions) {
        mContext = context;
        this.complaintSearchlist = complaintActions;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchComplaintAction>();
        this.arraylist.addAll(complaintActions);
    }

    @Override
    public int getCount() {
        return complaintSearchlist.size();
    }

    @Override
    public SearchComplaintAction getItem(int position) {
        return complaintSearchlist.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_action_complaint, null);

            holder.action_dtl = (TextView) view.findViewById(R.id.action_dtl);
            holder.action_ename = (TextView) view.findViewById(R.id.action_ename);
            holder.action_reason = (TextView) view.findViewById(R.id.action_reason);

            holder.action_date = (TextView) view.findViewById(R.id.action_date);
            holder.follow_up_date = (TextView) view.findViewById(R.id.follow_up_date);
            holder.action_txt = (TextView) view.findViewById(R.id.action_txt);


            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }


        String action_date, follow_up_date, action_txt = " ",action__reason_txt ="";


        action_date = complaintSearchlist.get(position).getDate();
        follow_up_date = complaintSearchlist.get(position).getFollow_up_date();
        action_txt = complaintSearchlist.get(position).getStatus();
        action__reason_txt = complaintSearchlist.get(position).getPending_reason();

        holder.action_dtl.setText(complaintSearchlist.get(position).getReason());
        holder.action_ename.setText(complaintSearchlist.get(position).getEname());

        holder.action_txt.setText("Action" + ": " + action_txt);

        if(action__reason_txt == null || action__reason_txt.isEmpty())
        {
            holder.action_reason.setVisibility(View.GONE);
        }
        else {
            holder.action_reason.setVisibility(View.VISIBLE);
            getPendingReasonname(action__reason_txt);
            holder.action_reason.setText("Pending Complaint Reason" + ": " + pending_category_name);

        }


        holder.action_date.setText("Date" + ": " + action_date);

        holder.follow_up_date.setText("Follow up " + ": " + follow_up_date);


        return view;
    }

    public class ViewHolder {

        TextView follow_up_date, action_dtl, action_ename, action_date, action_txt,action_reason;

    }

     public void getPendingReasonname(String pendingid) {

        DatabaseHelper dataHelper = new DatabaseHelper(mContext);

        SQLiteDatabase db = dataHelper.getReadableDatabase();
        // Start the transaction.
        db.beginTransactionNonExclusive();
        try {
            String selectQuery = "SELECT * FROM " + DatabaseHelper.TABLE_PENDING_REASON + " WHERE " + DatabaseHelper.KEY_PEND_NO + " = '" + pendingid + "'";


            Cursor cursor = db.rawQuery(selectQuery, null);

            Log.e("COUNT","&&&"+cursor.getCount());
            if (cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    pending_category_name = cursor.getString(cursor.getColumnIndex("name"));

                }

                db.setTransactionSuccessful();

            }
        } catch (SQLiteException e) {
            e.printStackTrace();

        } finally {
            if(db!=null) {
                db.endTransaction();
                // End the transaction.
                db.close();
                // Close database
            }

        }


    }

}
