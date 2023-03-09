package searchlist.complaint;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import backgroundservice.SyncDataService;
import bean.ComplaintForward;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class SearchForwardComplaintListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    String str_cmp_no = "";
    SharedPreferences pref;
    String forward_to = "null", forward_to_code = "null", cmpno = "",cmpepc = "";
    String sync_data_name = "null", sync_key_id = "null";
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<ComplaintForward> complaintSearchlist = null;
    private ArrayList<ComplaintForward> arraylist;
    private ProgressDialog progressDialog;

    public SearchForwardComplaintListViewAdapter(Context context, List<ComplaintForward> complaintForward, String lv_forward_to, String lv_cmpno, String lv_cmpepc) {
        mContext = context;
        forward_to = lv_forward_to;
        cmpno = lv_cmpno;
        cmpepc = lv_cmpepc;
        this.complaintSearchlist = complaintForward;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<ComplaintForward>();
        this.arraylist.addAll(complaintSearchlist);
        progressDialog = new ProgressDialog(mContext);
        pref = mContext.getSharedPreferences("MyPref", 0);
    }

    @Override
    public int getCount() {
        return complaintSearchlist.size();
    }

    @Override
    public ComplaintForward getItem(int position) {
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
            view = inflater.inflate(R.layout.search_listview_forward, null);

            holder.partner_code = (TextView) view.findViewById(R.id.partner_code);
            holder.partner_name = (TextView) view.findViewById(R.id.partner_name);

            holder.partner_code_txt = (TextView) view.findViewById(R.id.partner_code_txt);
            holder.partner_name_txt = (TextView) view.findViewById(R.id.partner_name_txt);


            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        // str_cmp_no = "Comp.No : " + complaintSearchlist.get(position).getCmpno() ;


//        holder.cmpno.setText( str_cmp_no );
        holder.partner_code.setText(complaintSearchlist.get(position).getPartner_code());
        holder.partner_name.setText(complaintSearchlist.get(position).getPartner_name());


        // holder.mobile_image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.call, 0);


        String name;

        name = forward_to + " Code : ";
        holder.partner_code_txt.setText(name);

        name = forward_to + " Name : ";
        holder.partner_name_txt.setText(name);


        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//                progressDialog = new ProgressDialog(mContext);
//                progressDialog.setMessage("Loading..."); // Setting Message
//                progressDialog.setTitle("Please wait..."); // Setting Title
//                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
//                progressDialog.show(); // Display Progress Dialog
//                progressDialog.setCancelable(false);
//
//
//                forward_to_code  = complaintSearchlist.get(position).getPartner_code();
//
//                new Worker().execute();


                AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
                alertDialog.setTitle("Forward complaint !");
                alertDialog.setMessage("Do you want to Forward complaint ?");

                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        progressDialog = new ProgressDialog(mContext);
                        progressDialog.setMessage("Loading..."); // Setting Message
                        progressDialog.setTitle("Please wait..."); // Setting Title
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
                        progressDialog.show(); // Display Progress Dialog
                        progressDialog.setCancelable(false);


                        forward_to_code = complaintSearchlist.get(position).getPartner_code();

                        if (cmpepc.equalsIgnoreCase("")) {

                            if (!forward_to.equalsIgnoreCase("Installer")) {

                                new Worker().execute();

                            } else {
                                Toast.makeText(mContext, "Complaint is not forwarded to Installer, Only forwarded to Service Center of Service Partner ", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if(cmpepc.equalsIgnoreCase("X"))
                        {
                            new Worker().execute();
                        }

                        }
                });

                // on pressing cancel button

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();


            }
        });


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        complaintSearchlist.clear();
        if (charText.length() == 0) {
            complaintSearchlist.addAll(arraylist);
        } else {
            for (ComplaintForward sc : arraylist) {

                if (
                        sc.getPartner_name().toLowerCase(Locale.getDefault()).contains(charText) ||
                                sc.getPartner_code().toLowerCase(Locale.getDefault()).contains(charText)

                ) {
                    complaintSearchlist.add(sc);
                }


            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {

        TextView cmpno, partner_code, partner_name, other, other1, partner_name_txt, partner_code_txt;

    }

    private class Worker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {

            JSONArray ja_complaint_forward = new JSONArray();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);
            final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
            param.clear();

            try {

                //  Log.d("forward_to2",forward_to);

                JSONObject jsonObj = new JSONObject();

                jsonObj.put("pernr", CustomUtility.getSharedPreferences(mContext,"pernr"));
                jsonObj.put("objs", CustomUtility.getSharedPreferences(mContext,"objs"));
                jsonObj.put("cmpno", cmpno);
                jsonObj.put("forward_to", forward_to);
                jsonObj.put("forward_to_code", forward_to_code);
                jsonObj.put("sernr", WebURL.Serial_no);

                ja_complaint_forward.put(jsonObj);

            } catch (Exception e) {
                e.printStackTrace();
            }


            param.add(new BasicNameValuePair("COMPLAINT_FORWARD", String.valueOf(ja_complaint_forward)));


            String data = null;


//
//                    new Thread() {
//
//                public void run() {

            if (CustomUtility.isOnline(mContext)) {

                try {

                    String obj = CustomHttpClient.executeHttpPost1(WebURL.SYNC_OFFLINE_DATA_TO_SAP, param);

                    //  Log.d("login_obj", "" + obj);

                    if (obj != null) {

                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
/******************************************************************************************/
/*                       get JSONwebservice Data
/******************************************************************************************/
                        JSONObject jo_success = new JSONObject(obj);
                        JSONArray ja_success = jo_success.getJSONArray("data_success");


                        // Log.d("login_obj2", "" + ja_success.length());
                        for (int i = 0; i < ja_success.length(); i++) {
                            JSONObject jo = ja_success.getJSONObject(i);

                            sync_data_name = jo.getString("sync_data");
                            sync_key_id = jo.getString("key_id");

                            //Log.d("login_obj1", "" + sync_data_name +"--"+sync_key_id);


                            if (sync_data_name.equalsIgnoreCase("complaint_forward") &&
                                    sync_key_id.equalsIgnoreCase("0")) {

                                Message msg4 = new Message();
                                msg4.obj = "Complaint successfully forward";
                                mHandler.sendMessage(msg4);

                                // Toast.makeText(mContext, "Data saved to server.", Toast.LENGTH_LONG).show();
                                ((Activity) mContext).finish();
                            }

                        }


                    } else {
                        if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        }
//                                Toast.makeText(getApplicationContext(), "Connection to server failed", Toast.LENGTH_LONG).show();
                    }

                } catch (Exception e) {
                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    Log.d("exce", "" + e);
                }


            } else {
                  if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                Message msg2 = new Message();
                msg2.obj = "No Internet Connection";
                mHandler.sendMessage(msg2);


            }


//                }
//
//            }.start();
//


            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
//            Log.i("SomeTag", System.currentTimeMillis() / 1000L
//                    + " post execute \n" + result);
              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
        }


    }
}