package searchlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;

public class VideoGalleryListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<VideoGallery> videoSearchesList = null;
    private ArrayList<VideoGallery> arraylist;
    private ProgressDialog progressDialog;

    public VideoGalleryListViewAdapter(Context context, List<VideoGallery> videoSearchesList) {
        mContext = context;
        this.videoSearchesList = videoSearchesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<VideoGallery>();
        progressDialog = new ProgressDialog(mContext);
        this.arraylist.addAll(videoSearchesList);
    }

    @Override
    public int getCount() {
        return videoSearchesList.size();
    }

    @Override
    public VideoGallery getItem(int position) {
        return videoSearchesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_video_gallery, null);

            holder.video_name = (TextView) view.findViewById(R.id.video_name);


            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews

        holder.video_name.setText(videoSearchesList.get(position).getVideo_name());


//
//        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


                progressDialog = ProgressDialog.show(mContext, "", "Loading..");

                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        if (CustomUtility.isOnline(mContext)) {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            String video_id = "vnd.youtube:" + videoSearchesList.get(position).getVideo_link();

                            // Log.d("video_id",""+   "\"" + video_id  +"\""   );
                            // Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:28eqWqHuSb8"));
                            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(video_id));
                            mContext.startActivity(i);

                        } else {
                              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                            Message msg = new Message();
                            msg.obj = "Please ON internet Connection for this function.";
                            mHandler.sendMessage(msg);
                        }

                    }
                }).start();


            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        videoSearchesList.clear();
        if (charText.length() == 0) {
            videoSearchesList.addAll(arraylist);
        } else {
            for (VideoGallery vg : arraylist) {
                if (vg.getVideo_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    videoSearchesList.add(vg);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {

        TextView video_name;


    }

}