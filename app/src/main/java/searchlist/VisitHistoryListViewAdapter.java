package searchlist;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import bean.CustomerDetailBean;

public class VisitHistoryListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    // public static  CustomerDetailBean customerdetailbean;
    CustomerDetailBean customerdetailbean;
    MediaPlayer mediaPlayer;
    File mediaFile;
    String file_name;
    private List<VisitHistory> visitHistories = null;
    private ArrayList<VisitHistory> arraylist;

    public VisitHistoryListViewAdapter(Context context, List<VisitHistory> visitHistories) {
        mContext = context;
        this.visitHistories = visitHistories;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<VisitHistory>();
        this.arraylist.addAll(visitHistories);

    }

    @Override
    public int getCount() {
        return visitHistories.size();
    }

    @Override
    public VisitHistory getItem(int position) {
        return visitHistories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_visit_item, null);
            // Locate the TextViews in listview_item.xml
            // holder.customer_number = (TextView) view.findViewById(R.id.customer_number );
            holder.ename = (TextView) view.findViewById(R.id.visit_person);
            holder.date = (TextView) view.findViewById(R.id.visit_date);
            holder.time = (TextView) view.findViewById(R.id.visit_time);
            holder.comment = (TextView) view.findViewById(R.id.visit_comment);
            holder.visit_type = (TextView) view.findViewById(R.id.visit_type);
            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        //  holder.customer_number.setText(customerSearchesList.get(position).getCustomer_number());
        holder.ename.setText(visitHistories.get(position).getEname());
        holder.date.setText(visitHistories.get(position).getBudat());
        // holder.time.setText(visitHistories.get(position).getTime());
        holder.comment.setText(visitHistories.get(position).getComment());
        holder.visit_type.setText(visitHistories.get(position).getVisit());


        holder.time.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        if (!TextUtils.isEmpty(visitHistories.get(position).getAudio_record())) {
            holder.time.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.visit_audio, 0);
        }

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


// custom dialog
                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.audio_dialog);
                dialog.setTitle("Play Audio");


                final Button playAudio = (Button) dialog.findViewById(R.id.play_audio);
                final Button stopAudio = (Button) dialog.findViewById(R.id.stop_audio);
                final Button cancel = (Button) dialog.findViewById(R.id.cancel);

                stopAudio.setTextColor(Color.parseColor("#ffffff"));
                stopAudio.setBackgroundColor(Color.parseColor("#8b8b8c"));

                /************* play audio file************************/
                playAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        playAudio.setTextColor(Color.parseColor("#ffffff"));
                        playAudio.setBackgroundColor(Color.parseColor("#8b8b8c"));

                        playAudio.setEnabled(false);


                        stopAudio.setEnabled(true);
                        stopAudio.setBackgroundColor(Color.parseColor("#0179b6"));
                        stopAudio.setTextColor(Color.parseColor("#ffffff"));


                        cancel.setEnabled(false);

                        cancel.setTextColor(Color.parseColor("#ffffff"));
                        cancel.setBackgroundColor(Color.parseColor("#8b8b8c"));


                        String audio;

                        if (!TextUtils.isEmpty(visitHistories.get(position).getAudio_record())) {

                            audio = visitHistories.get(position).getAudio_record();

                            byte[] encodeByte = Base64.decode(audio, Base64.DEFAULT);


                            mediaFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/"
                                    + "AUD_" + System.currentTimeMillis() + ".mp3");


                            try {
                                FileOutputStream fileoutputstream = new FileOutputStream(mediaFile.getPath());
                                fileoutputstream.write(encodeByte);
                                fileoutputstream.close();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }


                            if (mediaFile.exists()) {


                                try {


                                    mediaPlayer = new MediaPlayer();

                                    mediaPlayer.setDataSource(mediaFile.getPath());
                                    mediaPlayer.prepare();
                                    mediaPlayer.start();

                                } catch (IllegalStateException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }


                            }
                        }

                    }
                });


                /************* stop audio file************************/


                stopAudio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playAudio.setEnabled(true);
                        playAudio.setBackgroundColor(Color.parseColor("#0179b6"));
                        playAudio.setTextColor(Color.parseColor("#ffffff"));


                        stopAudio.setEnabled(false);
                        stopAudio.setTextColor(Color.parseColor("#ffffff"));
                        stopAudio.setBackgroundColor(Color.parseColor("#8b8b8c"));


                        //  mediaPlayer = new MediaPlayer();
                        if (mediaPlayer != null) {
                            mediaPlayer.stop();
                            mediaPlayer.release();

                            File file = new File(mediaFile.getPath());

                            if (file.exists()) {
                                file.delete();
                            }

                        }

                        dialog.dismiss();

                    }
                });


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });


                dialog.show();


            }
        });

        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        visitHistories.clear();
        if (charText.length() == 0) {
            visitHistories.addAll(arraylist);
        } else {
            for (VisitHistory cs : arraylist) {
                if ((cs.getEname().toLowerCase(Locale.getDefault()).contains(charText)) ||
                        (cs.getVisit().toLowerCase(Locale.getDefault()).contains(charText))) {
                    visitHistories.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView ename;
        TextView date;
        TextView time;
        TextView comment;
        TextView visit_type;


    }


}