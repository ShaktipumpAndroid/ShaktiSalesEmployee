package searchlist.complaint;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Message;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.Capture_employee_gps_location;
import activity.CustomUtility;
import activity.SurveyActivity;


public class SearchServiceCenterListViewAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    String str_cmp_no = "";
    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(mContext, mString, Toast.LENGTH_LONG).show();
        }
    };
    private List<SearchServiceCenter> serviceCenterList = null;
    private ArrayList<SearchServiceCenter> arraylist;
    private ProgressDialog progressDialog;

    public SearchServiceCenterListViewAdapter(Context context, List<SearchServiceCenter> serviceCenterList) {
        mContext = context;
        this.serviceCenterList = serviceCenterList;
        progressDialog = new ProgressDialog(mContext);
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchServiceCenter>();
        this.arraylist.addAll(serviceCenterList);
    }

    @Override
    public int getCount() {
        return serviceCenterList.size();
    }

    @Override
    public SearchServiceCenter getItem(int position) {
        return serviceCenterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_service_center, null);


            holder.service_center_code = (TextView) view.findViewById(R.id.service_center_code);
            holder.servive_center = (TextView) view.findViewById(R.id.service_center);
            holder.address = (TextView) view.findViewById(R.id.address);
            holder.pending_reason = view.findViewById(R.id.pending_reason);
            holder.mobile_no = (TextView) view.findViewById(R.id.mobile_no);
            holder.contact_person = (TextView) view.findViewById(R.id.contact_person);
            holder.employee_name = (TextView) view.findViewById(R.id.employee_name);
            holder.mobile_image = (TextView) view.findViewById(R.id.mobile_image);
            holder.btn_survey = (TextView) view.findViewById(R.id.btn_survey);


            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        //   Log.d("ser_mob",""+ serviceCenterList.get(position).getTelf1())  ;


//        holder.cmpno.setText( str_cmp_no );
        holder.service_center_code.setText(serviceCenterList.get(position).getKunnr());
        holder.servive_center.setText(serviceCenterList.get(position).getName1());
        holder.address.setText(serviceCenterList.get(position).getAddress());
        holder.mobile_no.setText(serviceCenterList.get(position).getTelf1());
        holder.contact_person.setText(serviceCenterList.get(position).getContact_person());
        holder.employee_name.setText(serviceCenterList.get(position).getEname());


//
        holder.mobile_image.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.call, 0);
//


        holder.btn_survey.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.survey, 0);

        if (!serviceCenterList.get(position).getLat_long().equalsIgnoreCase("0.0,0.0")) {
            holder.btn_survey.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.map, 0);
        } else {
            holder.btn_survey.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.survey, 0);
        }

        holder.mobile_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(serviceCenterList.get(position).getTelf1())) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(serviceCenterList.get(position).getTelf1());
                        }

                    } else {
                        Make_Call(serviceCenterList.get(position).getTelf1());
                    }
                }
            }
        });


        holder.mobile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(serviceCenterList.get(position).getTelf1())) {

                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions((Activity) mContext, new String[]{android.Manifest.permission.CALL_PHONE}, 222);
                        } else {
                            Make_Call(serviceCenterList.get(position).getTelf1());
                        }

                    } else {
                        Make_Call(serviceCenterList.get(position).getTelf1());
                    }
                }
            }
        });


        holder.btn_survey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!serviceCenterList.get(position).getLat_long().equalsIgnoreCase("0.0,0.0")) {

                    progressDialog = ProgressDialog.show(mContext, "", "Checking Internet Connection.");

                    new Thread(new Runnable() {
                        //
                        @Override
                        public void run() {

                            if (CustomUtility.isOnline(mContext)) {
                                  if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };


                                try {
                                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                            Uri.parse("geo:0,0?q=" + serviceCenterList.get(position).lat_long + " (" + "Shakti Service Center" + ")"));
                                    mContext.startActivity(intent);

                                } catch (Exception e) {
                                    System.out.println("Shakti Service Center");
                                }


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

                } else {


                    Intent intent = new Intent(mContext, SurveyActivity.class);
                    intent.putExtra("customer_name", serviceCenterList.get(position).getName1());
                    intent.putExtra("phone_number", serviceCenterList.get(position).getKunnr());
                    intent.putExtra("route_code", "NA");
                    mContext.startActivity(intent);

                }


            }
        });


//        holder.address.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                progressDialog = ProgressDialog.show(mContext, "", "Checking Internet Connection.");
//
//                new Thread(new Runnable() {
//                    //
//                    @Override
//                    public void run() {
//
//                        if (CustomUtility.isOnline(mContext)) {
//                              if ((progressDialog != null) && progressDialog.isShowing()) {

//                        String str = complaintSearchlist.get(position).getAddress() ;
//                        String url = "http://maps.google.co.in/maps?q="+ str;
//                        Intent intent = new Intent(Intent.ACTION_VIEW,  Uri.parse(url));
//                        mContext.startActivity(intent);
//                        }
//                        else
//                        {
//                              if ((progressDialog != null) && progressDialog.isShowing()) {

//                            Message msg = new Message();
//                            msg.obj = "Please ON internet Connection for this function.";
//                            mHandler.sendMessage(msg);
//                        }
//                    }
//                }).start();
//
//
//            }
//        });


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        serviceCenterList.clear();
        if (charText.length() == 0) {
            serviceCenterList.addAll(arraylist);
        } else {
            for (SearchServiceCenter vg : arraylist) {
                if (vg.getName1().toLowerCase(Locale.getDefault()).contains(charText) ||
                        vg.getKunnr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        vg.getAddress().toLowerCase(Locale.getDefault()).contains(charText)

                ) {
                    serviceCenterList.add(vg);
                }
            }
        }
        notifyDataSetChanged();
    }

    private void Make_Call(String mobile) {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            new Capture_employee_gps_location(mContext, "12", mobile);
            intent.setData(Uri.parse("tel:" + mobile));
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder {

        TextView contact_person, service_center_code, servive_center, address, pending_reason, mobile_no, dealer_name, employee_name, btn_gps, mobile_image, btn_survey, comp_dtl;

    }

}