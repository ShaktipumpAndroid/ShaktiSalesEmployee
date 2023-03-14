package adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.ByteArrayOutputStream;
import java.util.List;

import activity.CustomUtility;
import activity.complaint.PhotoViewerActivity;
import model.ComplaintImageModel;
import webservice.Constants;


public class PendingComplainGridListAdapter extends RecyclerView.Adapter<PendingComplainGridListAdapter.ViewHolder> {

    private Context mContext;

    private List<ComplaintImageModel.Response> complaintImageList;



    public PendingComplainGridListAdapter(Context mContext, List<ComplaintImageModel.Response> complaintImageList) {
        this.mContext = mContext;
         this.complaintImageList = complaintImageList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_grid_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        ComplaintImageModel.Response complaintImageModel = complaintImageList.get(position);
      //       holder.imgComplaint.setImageBitmap(CustomUtility.getBitmapFromBase64(complaintImageModel.getImage1().trim()));
        Glide.with(mContext).load(CustomUtility.getBitmapFromBase64(complaintImageModel.getImage1().trim())).placeholder(R.mipmap.ic_notification).dontAnimate().into(holder.imgComplaint);


        holder.imgComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*Intent intent = new Intent(mContext, PhotoViewerActivity.class);
                intent.putExtra(Constants.complaintImageModel, complaintImageModel);
                mContext.startActivity(intent);*/

                Bitmap bitmap = ((BitmapDrawable) holder.imgComplaint.getDrawable()).getBitmap();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageInByte = baos.toByteArray();
                Intent intent = new Intent(mContext, PhotoViewerActivity.class);
                intent.putExtra(Constants.complaintImageModel, imageInByte);
                mContext.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
       return complaintImageList.size();

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imgComplaint;

        public ViewHolder(View view) {
            super(view);

            imgComplaint = view.findViewById(R.id.imgComplaint);

        }
    }

}


