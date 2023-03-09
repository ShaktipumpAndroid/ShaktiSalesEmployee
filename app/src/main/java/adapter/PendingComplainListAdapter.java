package adapter;

import android.content.Context;
import android.content.Intent;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import activity.complaint.PendingComplainDetailActivity;
import activity.complaint.PendingComplainPhotoGridActivity;
import activity.complaint.PendingComplainRemarkListActivity;
import bean.vkbean.ComplainAllResponse;


public class PendingComplainListAdapter extends RecyclerView.Adapter<PendingComplainListAdapter.ViewHolder> {

    private Context mContext;
    private String mStatusValue;



    private List<ComplainAllResponse> mComplainAllResponse;

   /* public PendingComplainListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;

    }*/


    public PendingComplainListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse, String mStatusValue) {
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;
        this.mStatusValue = mStatusValue;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.txtDealerOEMValueID.setText(mComplainAllResponse.get(position).getName1());
        holder.txtComplainnoValueID.setText(mComplainAllResponse.get(position).getCmpno());
        holder.txtComplainDateValueID.setText(mComplainAllResponse.get(position).getCmpdt());
        holder.txtMOBNumberValueID.setText(mComplainAllResponse.get(position).getMblno());
        holder.txtCustomerNameValueID.setText(mComplainAllResponse.get(position).getCstname());
        holder.txtAddressValueID.setText(mComplainAllResponse.get(position).getCaddress());
       // holder.txtMaterialCodeValueID.setText(mComplainAllResponse.get(position).getC);

        //holder.txtSeriolNumberValueID.setText(mComplainAllResponse.get(position).getGstBillno());
     //   holder.txtMaterialDescValueID.setText(mComplainAllResponse.get(position).getGstBillno());
        holder.txtEngNameValueID.setText(mComplainAllResponse.get(position).getEname());
        holder.txtEngMobNoValueID.setText(mComplainAllResponse.get(position).getSengno());
      //  holder.lvlMainItemViewID.setTag(mComplainAllResponse.get(position));

        holder.txtViewPhotoID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent mIntent = new Intent(mContext, PendingComplainPhotoGridActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());

                mIntent.putExtra("StatusValue",mComplainAllResponse.get(position).getAtt_posnr());


                mContext.startActivity(mIntent);

            }
        });

        holder.txtViewRmarkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(mContext, PendingComplainRemarkListActivity.class);
                mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                mIntent.putExtra("StatusValue",mStatusValue);
                mIntent.putExtra("complaint", "Complaint Remark List");

                mContext.startActivity(mIntent);


            }
        });


        holder.txtClickHereID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mStatusValue.equalsIgnoreCase("01"))
                {
                    Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                    mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                    mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                    mIntent.putExtra("StatusValue","01");
                    mIntent.putExtra("complaint", "Pending Complaint");
                    mContext.startActivity(mIntent);
                }
                else  if(mStatusValue.equalsIgnoreCase("02"))
                {

                    Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                    mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                    mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                    mIntent.putExtra("StatusValue","02");
                    mIntent.putExtra("complaint", "Pending for Approval");
                    mContext.startActivity(mIntent);
                }

            }
        });

        holder.lvlMainItemViewID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              //  view.getTag();

                if(mStatusValue.equalsIgnoreCase("01"))
                {
                    Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                    mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                    mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                    mIntent.putExtra("StatusValue","01");
                    mIntent.putExtra("complaint", "Pending Complaint");
                    mContext.startActivity(mIntent);
                }
                else  if(mStatusValue.equalsIgnoreCase("02"))
                {

                    Intent mIntent = new Intent(mContext, PendingComplainDetailActivity.class);
                    mIntent.putExtra("Complain_number",mComplainAllResponse.get(position).getCmpno());
                    mIntent.putExtra("mobile_number",mComplainAllResponse.get(position).getMblno());
                    mIntent.putExtra("StatusValue","02");
                    mIntent.putExtra("complaint", "Pending for Approval");
                    mContext.startActivity(mIntent);
                }


            }
        });
      //  holder.txtWarrantyValueID.setText(mComplainAllResponse.get(position).getGstBillno());


    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mComplainAllResponse != null && mComplainAllResponse.size() > 0)
            return mComplainAllResponse.size();
        else
            return 0;
       //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {



        public TextView txtDealerOEMValueID, txtComplainnoValueID, txtComplainDateValueID, txtMOBNumberValueID,txtCustomerNameValueID,
                txtAddressValueID, txtMaterialCodeValueID, txtSeriolNumberValueID, txtMaterialDescValueID, txtEngNameValueID, txtEngMobNoValueID,txtWarrantyValueID;

        TextView txtViewPhotoID, txtViewRmarkID, txtClickHereID;

        public RelativeLayout rlvNotifyItemMainViewID;
        public LinearLayout lvlMainItemViewID;

        public ViewHolder(View v) {

            super(v);
            txtClickHereID = (TextView) v.findViewById(R.id.txtClickHereID);
            txtViewPhotoID = (TextView) v.findViewById(R.id.txtViewPhotoID);
            txtViewRmarkID = (TextView) v.findViewById(R.id.txtViewRmarkID);
            lvlMainItemViewID = (LinearLayout) v.findViewById(R.id.lvlMainItemViewID);
            txtDealerOEMValueID = (TextView) v.findViewById(R.id.txtDealerOEMValueID);
            txtComplainnoValueID =  (TextView) v.findViewById(R.id.txtComplainnoValueID);
            txtComplainDateValueID =  (TextView) v.findViewById(R.id.txtComplainDateValueID);
            txtMOBNumberValueID =   (TextView) v.findViewById(R.id.txtMOBNumberValueID);
            txtCustomerNameValueID =   (TextView) v.findViewById(R.id.txtCustomerNameValueID);
            txtAddressValueID =   (TextView) v.findViewById(R.id.txtAddressValueID);
          //  txtMaterialCodeValueID =   (TextView) v.findViewById(R.id.txtMaterialCodeValueID);
          //  txtSeriolNumberValueID =   (TextView) v.findViewById(R.id.txtSeriolNumberValueID);
         //   txtMaterialDescValueID =   (TextView) v.findViewById(R.id.txtMaterialDescValueID);
            txtEngNameValueID =   (TextView) v.findViewById(R.id.txtEngNameValueID);
            txtEngMobNoValueID =   (TextView) v.findViewById(R.id.txtEngMobNoValueID);
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


