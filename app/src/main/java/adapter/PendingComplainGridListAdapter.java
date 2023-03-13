package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;

import activity.complaint.PhotoViewerActivity;
import activity.complaint.ShowDocument1;
import bean.vkbean.RemarkResponseList;


public class PendingComplainGridListAdapter extends RecyclerView.Adapter<PendingComplainGridListAdapter.ViewHolder> {

    private Context mContext;
    private String mStatusValue;
    private String mMobileNumber;
    byte[] encodeByte;
    Bitmap bitmap;

    private List<RemarkResponseList> mRemarkResponseList;

   /* public PendingComplainListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;

    }*/
   ArrayList<String> al;

    public PendingComplainGridListAdapter(Context mContext, ArrayList<String> al, String mStatusValue) {
        this.mContext = mContext;
        this.al = al;
        this.mStatusValue = mStatusValue;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_grid_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

       // holder.imgGRIDID.setImageResource(Integer.parseInt(al.get(position)));
        if (al.get(position) != null && !al.get(position).isEmpty()) {
            encodeByte = Base64.decode(al.get(position), Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            holder.imgGRIDID.setImageBitmap(bitmap);
        }


        holder.imgGRIDID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i_display_image = new Intent(mContext, PhotoViewerActivity.class);
                Bundle extras = new Bundle();
                extras.putString("key", al.get(position));
                i_display_image.putExtras(extras);
                mContext.startActivity(i_display_image);


            }
        });


    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (al != null && al.size() > 0)
            return al.size();
        else
            return 0;

        //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {





        public TextView txtRemarkActionValueID, txtRemarkEnameValueID,txtRemarkComplainnoValueID,txtRemarkDateValueID;



        public RelativeLayout rlvNotifyItemMainViewID;
        public LinearLayout lvlMainItemViewID;
        public TextView txtBTNActionID, txtBTNPendingID, txtBTNClodeID, txtBTNUploadID;
        public ImageView imgGRIDID;

        public ViewHolder(View v) {

            super(v);

            imgGRIDID = (ImageView) v.findViewById(R.id.imgGRIDID);


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


