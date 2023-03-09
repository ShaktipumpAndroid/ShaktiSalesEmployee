package adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import activity.CustomUtility;
import activity.GPSTracker;
import activity.OfflineDataConveyance;
import bean.LocalConvenienceBean;
import bean.LoginBean;
import database.DatabaseHelper;
import models.DistanceResponse;
import models.Element;
import other.CameraUtils;
import rest.DistanceApiClient;
import rest.RestUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import webservice.CustomHttpClient;
import webservice.WebURL;


import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


public class Adapter_offline_list extends RecyclerView.Adapter<Adapter_offline_list.HomeCategoryViewHolder> {
    View.OnClickListener onclick_listener;
    DatabaseHelper db;
    private ProgressDialog progressDialog;
    String fullAddress = null;
    String fullAddress1 = null;
    String distance1 = null;
    String current_end_time, current_end_date;

    private Context context;
    private LocationManager locationManager;

    private ArrayList<LocalConvenienceBean> responseList;
    private CustomUtility customutility = null;
    String lt1= "";
    String lt2= "";
    String lg1= "";
    String lg2= "";
    String strtpht= "";
    String endpht= "";
    LocalConvenienceBean localConvenienceBean;
    LoginBean lb;
    public static TextView photo2;
    public static String end_photo_text;
    public static boolean end_photo_flag = false;



    public Adapter_offline_list(Context context, ArrayList<LocalConvenienceBean> responseList) {
        this.context = context;
        this.responseList = responseList;
        customutility = new CustomUtility();
        lb = new LoginBean();
        locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        progressDialog = new ProgressDialog(context);
        db = new DatabaseHelper(context);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_offline_list, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {

            if (!TextUtils.isEmpty(responseList.get(position).getBegda())) {

                holder.start_date.setText(responseList.get(position).getBegda());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getEndda())) {

                holder.end_date.setText(responseList.get(position).getEndda());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_time())) {

                holder.start_time.setText(responseList.get(position).getFrom_time());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getTo_time())) {

                holder.end_time.setText(responseList.get(position).getTo_time());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_lat())) {

                holder.start_lat.setText(responseList.get(position).getFrom_lat());

            }

            if (!TextUtils.isEmpty(responseList.get(position).getTo_lat())) {

                holder.end_lat.setText(responseList.get(position).getTo_lat());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getFrom_lng())) {

                holder.start_lng.setText(responseList.get(position).getFrom_lng());

            }
            if (!TextUtils.isEmpty(responseList.get(position).getTo_lng())) {

                holder.end_lng.setText(responseList.get(position).getTo_lng());

            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        //Toast.makeText(context, "GPS is Enabled in your device", Toast.LENGTH_SHORT).show();
                        if (CustomUtility.isOnline(context)) {
                            String start_lat,start_lng,end_lat,end_lng,start_dat,start_tm,end_dat,end_tm;



                                    start_lat = responseList.get(position).getFrom_lat();
                                    start_lng = responseList.get(position).getFrom_lng();
                                    end_lat   = responseList.get(position).getTo_lat();
                                    end_lng   = responseList.get(position).getTo_lng();

                            start_dat = responseList.get(position).getBegda();
                            start_tm = responseList.get(position).getFrom_time();
                            end_dat   = responseList.get(position).getEndda();
                            end_tm   = responseList.get(position).getTo_time();

                            startLocationUpdates1(start_lat,start_lng,end_lat,end_lng,start_dat,start_tm,end_dat,end_tm);

                        } else {
                            if (progressDialog != null)
                                  if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        String start_lat,start_lng,end_lat,end_lng,start_dat,start_tm,end_dat,end_tm;

                        start_lat = responseList.get(position).getFrom_lat();
                        start_lng = responseList.get(position).getFrom_lng();
                        end_lat   = responseList.get(position).getTo_lat();
                        end_lng   = responseList.get(position).getTo_lng();

                        start_dat = responseList.get(position).getBegda();
                        start_tm = responseList.get(position).getFrom_time();
                        end_dat   = responseList.get(position).getEndda();
                        end_tm   = responseList.get(position).getTo_time();
                        buildAlertMessageNoGps1(start_lat,start_lng,end_lat,end_lng,start_dat,start_tm,end_dat,end_tm);
                    }
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return responseList.size();
    }

    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView start_date, end_date,start_time,end_time,start_lat,end_lat,start_lng,end_lng;

        CardView cardView;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);

            start_date = itemView.findViewById(R.id.start_date);
            end_date = itemView.findViewById(R.id.end_date);
            start_time = itemView.findViewById(R.id.start_time);
            end_time = itemView.findViewById(R.id.end_time);
            start_lat = itemView.findViewById(R.id.start_lat);
            end_lat = itemView.findViewById(R.id.end_lat);
            start_lng = itemView.findViewById(R.id.start_lng);
            end_lng = itemView.findViewById(R.id.end_lng);


            cardView = itemView.findViewById(R.id.card_view);

        }
    }


    private void startLocationUpdates1(String startlat,String startlng,String endlat,String endlng,String startdat,String starttm,String enddat,String endtm) {


        if (ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                &&  ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(context, "You need to enable permissions to display location !", Toast.LENGTH_SHORT).show();
        }


        GPSTracker gps = new GPSTracker(context);

        if (gps.canGetLocation()) {

                if(CustomUtility.isOnline(context))
                {
                    progressDialog = ProgressDialog.show(context, "Loading...", "Please wait !");
                    double strtlat = Double.parseDouble(startlat);
                    double strtlng = Double.parseDouble(startlng);
                    double edlat = Double.parseDouble(endlat);
                    double edlng = Double.parseDouble(endlng);

                    getDistanceInfo(strtlat,strtlng, edlat,edlng,startdat,starttm,enddat,endtm);
                }
                else{

                    Toast.makeText(context, "Please Connect to Internet...,Your Data is Saved to the Offline Mode.", Toast.LENGTH_SHORT).show();

                }


        } else {
            gps.showSettingsAlert();
        }

    }


    private void buildAlertMessageNoGps1(final String startlat, final String startlng, final String endlat, final String endlng, final String startdat, final String starttm, final String enddat, final String endtm) {


        if(CustomUtility.isOnline(context))
        {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Please turn on the GPRS and keep it on while traveling on tour/trip.")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        context.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));

                            //getGpsLocation1();
                            startLocationUpdates1(startlat,startlng,endlat,endlng,startdat,starttm,enddat,endtm);


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
                } else {
            Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void getDistanceInfo(final double lat1, final double lon1, final double lat2, final double lon2, final String strtdat, final String strttm, final String enddat , final String endtm) {
        // http://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=Washington,DC&destinations=New+York+City,NY
        Map<String, String> mapQuery = new HashMap<>();

        mapQuery.put("origins", lat1+","+lon1);
        mapQuery.put("destinations", lat2+","+lon2);
        mapQuery.put("units", "metric");
        mapQuery.put("mode", "driving");
        mapQuery.put("key", "AIzaSyAohhwZ11LRwoxsS8lJ0VHGkA4L-cwjWmw");

        DistanceApiClient client = RestUtil.getInstance().getRetrofit().create(DistanceApiClient.class);

        Call<DistanceResponse> call = client.getDistanceInfo(mapQuery);
        call.enqueue(new Callback<DistanceResponse>() {
            @Override
            public void onResponse(Call<DistanceResponse> call, Response<DistanceResponse> response) {
                if (response.body() != null &&
                        response.body().getRows() != null &&
                        response.body().getRows().size() > 0 &&
                        response.body().getRows().get(0) != null &&
                        response.body().getRows().get(0).getElements() != null &&
                        response.body().getRows().get(0).getElements().size() > 0 &&
                        response.body().getRows().get(0).getElements().get(0) != null &&
                        response.body().getRows().get(0).getElements().get(0).getDistance() != null &&
                        response.body().getRows().get(0).getElements().get(0).getDuration() != null) {

                    try {

                        if(progressDialog!=null)
                              if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                        Element element = response.body().getRows().get(0).getElements().get(0);
                        fullAddress = response.body().getOriginAddresses().get(0);
                        fullAddress1 = response.body().getDestinationAddresses().get(0);
                        distance1 = element.getDistance().getText();


                        localConvenienceBean = db.getLocalConvinienceData();

                        strtpht = localConvenienceBean.getPhoto1();
                        endpht = localConvenienceBean.getPhoto2();

                        Log.e("111","1111"+fullAddress);
                        Log.e("222","2222"+fullAddress1);
                        Log.e("333","3333"+distance1);

                         lt1= String.valueOf(lat1);
                         lt2= String.valueOf(lat2);
                         lg1= String.valueOf(lon1);
                         lg2= String.valueOf(lon2);

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setCancelable(false);
                        dialog.setContentView(R.layout.custom_dialog2);
                        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                        lp.copyFrom(dialog.getWindow().getAttributes());
                        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                        dialog.getWindow().setAttributes(lp);

                        final TextInputEditText etstrdt = dialog.findViewById(R.id.tiet_str_dt);
                        final TextInputEditText etstrlatlng = dialog.findViewById(R.id.tiet_str_lat_lng);
                        final TextInputEditText etstrlocadd = dialog.findViewById(R.id.tiet_str_loc_add);
                        final TextInputEditText etenddt = dialog.findViewById(R.id.tiet_end_dt);
                        final TextInputEditText etendlatlng = dialog.findViewById(R.id.tiet_end_lat_lng);
                        final TextInputEditText etendlocadd = dialog.findViewById(R.id.tiet_end_loc_add);
                        final TextInputEditText ettotdis = dialog.findViewById(R.id.tiet_tot_dis);
                        final TextInputEditText ettrvlmod = dialog.findViewById(R.id.tiet_trvl_mod);
                        final TextView etcncl = dialog.findViewById(R.id.btn_cncl);
                        final TextView etconfm = dialog.findViewById(R.id.btn_cnfrm);
                        final TextView ettxt1 = dialog.findViewById(R.id.txt1);
                        final TextView ettxt2 = dialog.findViewById(R.id.txt2);
                        photo2 = dialog.findViewById(R.id.photo2);
                        ettrvlmod.requestFocus();

                        etstrdt.setText(strtdat +" "+ strttm);
                        etstrlatlng.setText(lt1 +","+ lg1);
                        etenddt.setText(enddat +" "+ endtm);
                        etendlatlng.setText(lt2 +","+ lg2);
                        etstrlocadd.setText(fullAddress);
                        etendlocadd.setText(fullAddress1);
                        ettotdis.setText(distance1);

                        ettxt1.setText("Local Conveyance Details");
                        ettxt2.setText("Press Confirm will end your Journey");

                        etcncl.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });

                        photo2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (end_photo_text == null || end_photo_text.isEmpty()) {

                                    showConfirmationGallery(DatabaseHelper.KEY_PHOTO2, "PHOTO2");
                                }
                            }
                        });

                        etconfm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String travel_mode = ettrvlmod.getText().toString();

                                if (CustomUtility.isOnline(context)) {

                                    if (!TextUtils.isEmpty(travel_mode) && !travel_mode.equals("")) {
                                        progressDialog = ProgressDialog.show(context, "", "Sending Data to server..please wait !");
                                        new Thread(new Runnable() {
                                            public void run() {
                                                ((Activity) context).runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        LocalConvenienceBean localConvenienceBean = new LocalConvenienceBean(LoginBean.getUseid(), strtdat,
                                                                enddat,
                                                                strttm,
                                                                endtm,
                                                                lt1,
                                                                lt2,
                                                                lg1,
                                                                lg2,
                                                                fullAddress,
                                                                fullAddress1,
                                                                distance1,strtpht,endpht);

                                                        db.updateLocalconvenienceData(localConvenienceBean);
                                                        SyncLocalConveneinceDataToSap(travel_mode,strtdat,strttm);
                                                    }
                                                });
                                            };
                                        }).start();

                                        dialog.dismiss();

                                    } else {
                                        Toast.makeText(context, "Please Enter Travel Mode.", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(context, "Please Connect to Internet...", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

                        dialog.show();


                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                        if(progressDialog!=null)
                              if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                    }
                }
            }

            @Override
            public void onFailure(Call<DistanceResponse> call, Throwable t) {

                if(progressDialog!=null)
                      if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

            }
        });
    }


    public void showConfirmationGallery(final String keyimage, final String name) {

        final CustomUtility customUtility = new CustomUtility();

        final CharSequence[] items = {"Take Photo", "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context, R.style.MyDialogTheme);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = CustomUtility.checkPermission(context);
                if (items[item].equals("Take Photo")) {

                    if (result) {
                        OfflineDataConveyance.openCamera(name);
                        setFlag(keyimage);
                    }

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    public void setFlag(String key) {
        end_photo_flag = false;

        switch (key) {

            case DatabaseHelper.KEY_PHOTO2:
                end_photo_flag = true;
                break;

        }

    }

    public void SyncLocalConveneinceDataToSap(String mode, String endat, String endtm) {

        String docno_sap = null;
        String invc_done = null;

        DatabaseHelper db = new DatabaseHelper(this.context);

        LocalConvenienceBean param_invc = new LocalConvenienceBean();

        param_invc = db.getLocalConvinienceData(endat,endtm);

        JSONArray ja_invc_data = new JSONArray();

        JSONObject jsonObj = new JSONObject();

        try {

            jsonObj.put("pernr", param_invc.getPernr());
            jsonObj.put("begda", CustomUtility.formateDate(param_invc.getBegda()));
            jsonObj.put("endda", CustomUtility.formateDate(param_invc.getEndda()));
            jsonObj.put("start_time",CustomUtility.formateTime( param_invc.getFrom_time()));
            jsonObj.put("end_time", CustomUtility.formateTime(param_invc.getTo_time()));
            jsonObj.put("start_lat", param_invc.getFrom_lat());
            jsonObj.put("end_lat", param_invc.getTo_lat());
            jsonObj.put("start_long", param_invc.getFrom_lng());
            jsonObj.put("end_long", param_invc.getTo_lng());
            jsonObj.put("start_location", param_invc.getStart_loc());
            jsonObj.put("end_location", param_invc.getEnd_loc());
            jsonObj.put("distance",  param_invc.getDistance());
            jsonObj.put("TRAVEL_MODE",  mode);
            jsonObj.put("PHOTO1",  param_invc.getPhoto1());
            jsonObj.put("PHOTO2",  param_invc.getPhoto2());

            ja_invc_data.put(jsonObj);

        } catch (Exception e) {
            e.printStackTrace();
        }

        final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
        param1_invc.add(new BasicNameValuePair("travel_distance", String.valueOf(ja_invc_data)));

        try {

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
            StrictMode.setThreadPolicy(policy);

            String obj2 = CustomHttpClient.executeHttpPost1(WebURL.LOCAL_CONVENIENVCE, param1_invc);

            if (obj2 != "") {

                JSONArray ja = new JSONArray(obj2);

                for (int i = 0; i < ja.length(); i++) {

                    JSONObject jo = ja.getJSONObject(i);

                    invc_done = jo.getString("msgtyp");
                    docno_sap = jo.getString("msg");

                    if (invc_done.equalsIgnoreCase("S")) {

                        ((Activity)context).finish();
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);
                        db.deleteLocalconvenienceDetail(endat,endtm);


                    } else if (invc_done.equalsIgnoreCase("E")) {
                          if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
                        Message msg = new Message();
                        msg.obj = docno_sap;
                        mHandler.sendMessage(msg);

                    }

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
              if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };
        }
    }


    android.os.Handler mHandler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            String mString = (String) msg.obj;
            Toast.makeText(context, mString, Toast.LENGTH_LONG).show();
        }
    };


//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        //super.onActivityResult(requestCode, resultCode, data);
//
//        // if the result is capturing Image
//        Bitmap bitmap = null;
//        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
//            if (resultCode == RESULT_OK) {
//
//                try {
//
//                    bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
//
//                    int count = bitmap.getByteCount();
//
//                    Log.e("Count", "&&&&&" + count);
//
//                    ByteArrayOutputStream byteArrayBitmapStream = new ByteArrayOutputStream();
//
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayBitmapStream);
//
//                    byte[] byteArray = byteArrayBitmapStream.toByteArray();
//
//                    long size = byteArray.length;
//
//                    Log.e("SIZE1234", "&&&&" + size);
//
//                    Log.e("SIZE1234", "&&&&" + Arrays.toString(byteArray));
//
//                    if (end_photo_flag) {
//                        end_photo_text = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                        setIcon(DatabaseHelper.KEY_PHOTO2);
//                    }
//
//
//                } catch (NullPointerException e) {
//                    e.printStackTrace();
//                }
//
//                File file = new File(imageStoragePath);
//                if (file.exists()) {
//                    file.delete();
//                }
//
//
//            }
//        }
//
//    }

}