package adapter;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.List;

import bean.vkbean.ComplainDetailListResponse;
import bean.vkbean.RemarkResponseList;


public class PendingComplainRemarkListAdapter extends RecyclerView.Adapter<PendingComplainRemarkListAdapter.ViewHolder> {

    private Context mContext;
    private String mStatusValue;
    private String mMobileNumber;

    private List<RemarkResponseList> mRemarkResponseList;

   /* public PendingComplainListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;

    }*/
    public PendingComplainRemarkListAdapter(Context mContext, List<RemarkResponseList> mRemarkResponseList, String mStatusValue, String mMobileNumber) {
        this.mContext = mContext;
        this.mRemarkResponseList = mRemarkResponseList;
        this.mStatusValue = mStatusValue;
        this.mMobileNumber = mMobileNumber;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_remark_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

       holder.txtRemarkActionValueID.setText(mRemarkResponseList.get(position).getAction());
       holder.txtRemarkEnameValueID.setText(mRemarkResponseList.get(position).getEname());
       holder.txtRemarkComplainnoValueID.setText(mRemarkResponseList.get(position).getCmpno());
       holder.txtRemarkDateValueID.setText(mRemarkResponseList.get(position).getAedtm());


    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mRemarkResponseList != null && mRemarkResponseList.size() > 0)
            return mRemarkResponseList.size();
        else
            return 0;
       //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

public TextView txtRemarkActionValueID, txtRemarkEnameValueID,txtRemarkComplainnoValueID,txtRemarkDateValueID;



        public RelativeLayout rlvNotifyItemMainViewID;
        public LinearLayout lvlMainItemViewID;
        public TextView txtBTNActionID, txtBTNPendingID, txtBTNClodeID, txtBTNUploadID;

        public ViewHolder(View v) {

            super(v);

            txtRemarkActionValueID = (TextView) v.findViewById(R.id.txtRemarkActionValueID);
            txtRemarkEnameValueID = (TextView) v.findViewById(R.id.txtRemarkEnameValueID);
            txtRemarkComplainnoValueID = (TextView) v.findViewById(R.id.txtRemarkComplainnoValueID);
            txtRemarkDateValueID = (TextView) v.findViewById(R.id.txtRemarkDateValueID);


            lvlMainItemViewID = (LinearLayout) v.findViewById(R.id.lvlMainItemViewID);

          //  txtWarrantyValueID =   (TextView) v.findViewById(R.id.txtWarrantyValueID);

          }
    }





    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };


}


