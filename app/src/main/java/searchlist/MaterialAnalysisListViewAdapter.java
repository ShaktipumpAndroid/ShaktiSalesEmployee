package searchlist;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import activity.DisplayStockActivity;
import webservice.CustomHttpClient;
import webservice.WebURL;

public class MaterialAnalysisListViewAdapter extends BaseAdapter {
    String stock = "", color = "";
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
    private ProgressDialog progressDialog;
    private List<MaterialAnalysisSearch> materialSearchesList = null;
    private ArrayList<MaterialAnalysisSearch> arraylist;

    public MaterialAnalysisListViewAdapter(Context context, List<MaterialAnalysisSearch> materialSearchesList) {
        mContext = context;
        this.materialSearchesList = materialSearchesList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<MaterialAnalysisSearch>();
        progressDialog = new ProgressDialog(mContext);
        this.arraylist.addAll(materialSearchesList);
    }

    @Override
    public int getCount() {
        return materialSearchesList.size();
    }

    @Override
    public MaterialAnalysisSearch getItem(int position) {
        return materialSearchesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_listview_material_analysis, null);
            // Locate the TextViews in listview_item.xml
            // holder.customer_number = (TextView) view.findViewById(R.id.customer_number );
            holder.matnr = (TextView) view.findViewById(R.id.matnr);
            holder.indicator = (TextView) view.findViewById(R.id.indicator);
            holder.maktx = (TextView) view.findViewById(R.id.maktx);
            holder.model = (TextView) view.findViewById(R.id.model);
            holder.plant = (TextView) view.findViewById(R.id.plant);
            holder.indicator = (TextView) view.findViewById(R.id.indicator);
            holder.price = (TextView) view.findViewById(R.id.price);
            holder.time = (TextView) view.findViewById(R.id.delivery_time);
            // holder.currency = (TextView) view.findViewById(R.id.currencey);

            view.setTag(holder);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews


        holder.matnr.setText(materialSearchesList.get(position).getMatnr());
        holder.maktx.setText(materialSearchesList.get(position).getMaktx());
        holder.model.setText(materialSearchesList.get(position).getExtwg());
        holder.plant.setText(materialSearchesList.get(position).getPlant());
        holder.indicator.setText(materialSearchesList.get(position).getIndicator());
        holder.time.setText(materialSearchesList.get(position).getDelivery_time());

        String lp_price = "Price : " + materialSearchesList.get(position).getKbetr() + "   " + materialSearchesList.get(position).getKonwa();

        holder.price.setText(lp_price);
        //holder.currency.setText(materialSearchesList.get(position).getKonwa());

//        holder.customer_distance.setText(customerSearchesList.get(position).getCountry());
//
        holder.plant.setTextColor(Color.parseColor("#666666"));

        if (!materialSearchesList.get(position).getPlant().equalsIgnoreCase("Shakti-CWC--1203")) {
            holder.plant.setTextColor(Color.parseColor("#0179b6"));
        }


        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {


//                final Dialog dialog = new Dialog( mContext);
//                dialog.setContentView(R.layout.stock_dialog);
//                dialog.setTitle("Stock Details");
//

//
//
                progressDialog = ProgressDialog.show(mContext, "", "Fetching stock data..");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                final ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();
                param.clear();
                param.add(new BasicNameValuePair("werks", materialSearchesList.get(position).getPlant()));
                param.add(new BasicNameValuePair("matnr", materialSearchesList.get(position).getMatnr()));


                new Thread(new Runnable() {


                    @Override
                    public void run() {


                        if (CustomUtility.isOnline(mContext)) {


                            try {
                                String obj = "";


                                obj = CustomHttpClient.executeHttpPost1(WebURL.MATERIAL_STOCK, param);
                                if (!TextUtils.isEmpty(obj)) {

                                      if ((progressDialog != null) && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            progressDialog = null;
                        };

                                    JSONArray ja = new JSONArray(obj);

                                    Log.e("Array", "&&&&" + ja.toString());

                                    for (int i = 0; i < ja.length(); i++) {
                                        JSONObject jo = ja.getJSONObject(i);


                                        stock = jo.getString("STOCK");
                                        color = jo.getString("COLOR");
                                        Log.d("ja", "" + color);

                                        Intent intent = new Intent(mContext, DisplayStockActivity.class);
                                        intent.putExtra("plant_stock", stock);
                                        intent.putExtra("plant_name", materialSearchesList.get(position).getPlant());
                                        intent.putExtra("plant_matnr", materialSearchesList.get(position).getMatnr());
                                        intent.putExtra("plant_maktx", materialSearchesList.get(position).getMaktx());
                                        intent.putExtra("plant_color", color);

                                        mContext.startActivity(intent);


//                                    login = jo.getString("LOGIN");
//                                    ename = jo.getString("NAME");
//
//                                    Log.d("succ", "" + login);

                                    }


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

                            Message msg = new Message();
                            msg.obj = "Please on internet Connection for this function.";
                            mHandler.sendMessage(msg);


                        }


                    }
                }).start();

//
//
//
//
//
////                final Dialog dialog = new Dialog( mContext);
////                dialog.setContentView(R.layout.stock_dialog);
//////                dialog.setTitle("Stock Details");
//
////
////                final TextView tv_werks = (TextView) dialog.findViewById(R.id.werks);
////                final TextView tv_matnr = (TextView) dialog.findViewById(R.id.matnr);
////                final TextView tv_stock = (TextView) dialog.findViewById(R.id.stock);
//
//
//                tv_stock.setText(stock);
//
//                dialog.show();


//
//                Intent intent = new Intent(mContext, DisplayStockActivity.class);
//                intent.putExtra("plant_stock",materialSearchesList.get(position).getPlant());
//                intent.putExtra("plant_name",materialSearchesList.get(position).getPlant());
//                intent.putExtra("plant_material",materialSearchesList.get(position).getMatnr());
//                mContext.startActivity(intent);
//


            }
        });


        return view;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        materialSearchesList.clear();
        if (charText.length() == 0) {
            materialSearchesList.addAll(arraylist);
        } else {
            for (MaterialAnalysisSearch cs : arraylist) {
                if (cs.getMatnr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getMaktx().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getPlant().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getIndicator().toLowerCase(Locale.getDefault()).contains(charText) ||
                        cs.getExtwg().toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                    materialSearchesList.add(cs);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder {
        TextView plant;
        TextView model;
        TextView time;
        TextView price;
        TextView currency;
        TextView matnr;
        TextView maktx;
        TextView indicator;


    }


}