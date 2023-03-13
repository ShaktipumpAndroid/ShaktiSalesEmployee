package adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import activity.CustomUtility;
import bean.vkbean.ComplainDetailListResponse;
import webservice.CustomHttpClient;
import webservice.WebURL;

import static webservice.SAPWebService.MODE_PRIVATE;


public class PendingComplainDetailsListAdapter extends RecyclerView.Adapter<PendingComplainDetailsListAdapter.ViewHolder> {

    private Context mContext;
    private String mStatusValue;
    private String textRemarkValue;
    private String mMobileNumber;
    private String mUserID;
    private String mStatusAC_RJ;
    private String mComplainNO;
    private Dialog dialog;

    private List<ComplainDetailListResponse> mComplainDetailListResponse;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

   /* public PendingComplainListAdapter(Context mContext, List<ComplainAllResponse> mComplainAllResponse) {
        // this.galleryModelsList = galleryModelsList;
        this.mContext = mContext;
        this.mComplainAllResponse = mComplainAllResponse;

    }*/
    public PendingComplainDetailsListAdapter(Context mContext, List<ComplainDetailListResponse> mComplainDetailListResponse, String mStatusValue, String mMobileNumber) {
        this.mContext = mContext;
        this.mComplainDetailListResponse = mComplainDetailListResponse;
        this.mStatusValue = mStatusValue;
        this.mMobileNumber = mMobileNumber;

       // mUserID = CustomUtility.getSharedPreferences(mContext,"userID");
        pref = this.mContext.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        editor = pref.edit();

        mUserID = pref.getString("key_username", "userid");
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view1 = LayoutInflater.from(mContext).inflate(R.layout.pending_comp_details_item_row, parent, false);
        ViewHolder viewHolder1 = new ViewHolder(view1);
        return viewHolder1;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") int position) {

       holder.txtwarranteeValueID.setText(mComplainDetailListResponse.get(position).getWarrantee());

       holder.txtComplainDateValueID.setText(mComplainDetailListResponse.get(position).getWarDate());

       holder.txtWarrenteeConditionsValueID.setText(mComplainDetailListResponse.get(position).getWarrantyCondition());
       holder.txtMaterialDescValueID.setText(mComplainDetailListResponse.get(position).getMaktx());
       holder.txtMaterialNoValueID.setText(mComplainDetailListResponse.get(position).getMatnr());
       holder.txtComplainnoValueID.setText(mComplainDetailListResponse.get(position).getCmpno());
       holder.txtsernrValueID.setText(mComplainDetailListResponse.get(position).getSernr());
       holder.txtReasonValueID.setText(mComplainDetailListResponse.get(position).getReason());
       holder.txtMOBNumberValueID.setText(mMobileNumber);

        holder.imgShareID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String shareData = "Complain No.: "+mComplainDetailListResponse.get(position).getCmpno()+"\n\nMaterial Code: "+mComplainDetailListResponse.get(position).getMatnr()+"\n\nWarranty : "+mComplainDetailListResponse.get(position).getWarrantee()+"\n\nWarranty condition: "
                        +mComplainDetailListResponse.get(position).getWarrantyCondition()+"\n\nSerial no.:"+mComplainDetailListResponse.get(position).getSernr()+"\n\nMaterial Desc.: "
                        +mComplainDetailListResponse.get(position).getMaktx()+"\n\nReason: "+mComplainDetailListResponse.get(position).getReason();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, shareData);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                mContext.startActivity(shareIntent);

            }
        });





       holder.txtAcceptBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               mStatusAC_RJ = "accepted";
               mComplainNO = mComplainDetailListResponse.get(position).getCmpno();

               initRemarkViewBoxAccept(v);


           }
       });

       holder.txtRejectBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               mStatusAC_RJ = "reject";
               mComplainNO = mComplainDetailListResponse.get(position).getCmpno();
               initRemarkViewBoxAccept(v);

           }
       });

      /* holder.txtForwardBTNID.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });*/






    }

    @Override
    public int getItemCount() {
        // return galleryModelsList.size();
        if (mComplainDetailListResponse != null && mComplainDetailListResponse.size() > 0)
            return mComplainDetailListResponse.size();
        else
            return 0;
       //  return 5;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

public TextView txtwarranteeValueID, txtMaterialDescValueID,txtComplainDateValueID,txtMaterialNoValueID, txtComplainnoValueID, txtMOBNumberValueID, txtsernrValueID, txtReasonValueID, txtWarrenteeConditionsValueID;


        public RelativeLayout rlvNotifyItemMainViewID;
        public LinearLayout lvlMainItemViewID;
        public TextView txtAcceptBTNID, txtRejectBTNID, txtForwardBTNID, txtBTNUploadID;
        public ImageView imgShareID;

        public ViewHolder(View v) {

            super(v);

            imgShareID = (ImageView) v.findViewById(R.id.imgShareID);
            txtAcceptBTNID = (TextView) v.findViewById(R.id.txtAcceptBTNID);
            txtRejectBTNID = (TextView) v.findViewById(R.id.txtRejectBTNID);
          //  txtForwardBTNID = (TextView) v.findViewById(R.id.txtForwardBTNID);


            txtWarrenteeConditionsValueID = (TextView) v.findViewById(R.id.txtWarrenteeConditionsValueID);
            txtwarranteeValueID = (TextView) v.findViewById(R.id.txtwarranteeValueID);
            txtMaterialDescValueID = (TextView) v.findViewById(R.id.txtMaterialDescValueID);
            txtComplainDateValueID = (TextView) v.findViewById(R.id.txtComplainDateValueID);
            txtMaterialNoValueID = (TextView) v.findViewById(R.id.txtMaterialNoValueID);
            txtComplainnoValueID = (TextView) v.findViewById(R.id.txtComplainnoValueID);
            txtMOBNumberValueID = (TextView) v.findViewById(R.id.txtMOBNumberValueID);
            txtsernrValueID = (TextView) v.findViewById(R.id.txtsernrValueID);
            txtReasonValueID = (TextView) v.findViewById(R.id.txtReasonValueID);





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


    private void initRemarkViewBoxAccept(View view) {

        EditText editText;
        TextView txtBTNRemarkID;
        dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_remark_as_complain);

        editText = dialog.findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = dialog.findViewById(R.id.txtBTNRemarkID);
        dialog.setCancelable(true);

        dialog.show();

        /*AlertDialog.Builder builder = new AlertDialog.Builder(PendingComplainDetailActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_remark_as_complain, viewGroup, false);
        editText = findViewById(R.id.edtRemarkViewIDD);
        txtBTNRemarkID = findViewById(R.id.txtBTNRemarkID);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(true);
        alertDialog.show();*/




        txtBTNRemarkID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                textRemarkValue = editText.getText().toString().trim();
                if(!textRemarkValue.equalsIgnoreCase(""))
                {
                    // callInsertRemarkAPI(textRemarkValue);
                   new callInsertAcceptRemarkAPI().execute();
                }
                else
                {
                    Toast.makeText(mContext, "Please enter remark!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }




    private class callInsertAcceptRemarkAPI extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            progressDialog = new ProgressDialog(mContext);
            progressDialog = ProgressDialog.show(mContext, "", "Sending Data to server..please wait !");

        }

        @Override
        protected String doInBackground(String... params) {
            String docno_sap = null;
            String invc_done = null;
            String obj2 = null;




            JSONArray ja_invc_data = new JSONArray();

            JSONObject jsonObj = new JSONObject();

            try {

                // jsonObj.put("project_no", projno);
                jsonObj.put("cmpno",mComplainNO);
                // jsonObj.put("beneficiary",ben);
                //  jsonObj.put("mobno",mLrInvoiceResponse.get(0).getMobno());
               // jsonObj.put("pernr", WebURL.MAIN_USER_ID);
                jsonObj.put("pernr", mUserID);

                jsonObj.put("status", mStatusAC_RJ);

                jsonObj.put("action", textRemarkValue);

                ja_invc_data.put(jsonObj);


            } catch (Exception e) {
                e.printStackTrace();
            }


            final ArrayList<NameValuePair> param1_invc = new ArrayList<NameValuePair>();
            param1_invc.add(new BasicNameValuePair("engineer_action", String.valueOf(ja_invc_data)));///array name lr_save
            Log.e("DATA", "$$$$" + param1_invc.toString());

            System.out.println("param1_invc==>>"+param1_invc);

            System.out.println("param1_invc==>>"+param1_invc.toString());

            try {

                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().build();
                StrictMode.setThreadPolicy(policy);

                //obj2 = CustomHttpClient.executeHttpPost1(WebURL.SAVE_INSTALLATION_DATA, param1_invc);
                obj2 = CustomHttpClient.executeHttpPost1(WebURL.PENDING_COMPLAIN_SAVE_ENG_ACTION_VK_PAGE, param1_invc);

                Log.e("OUTPUT1", "&&&&" + obj2);

                if (obj2 != "") {
                    JSONObject object = new JSONObject(obj2);
                    String mStatus = object.getString("status");
                    final String mMessage = object.getString("message");
                    String jo11 = object.getString("response");
                    System.out.println("jo11==>>"+jo11);
                    if (mStatus.equalsIgnoreCase("true")) {
                        Message msg = new Message();
                        msg.obj = "Data Submitted Successfully...";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                        //  finish();
                    }
                    else
                    {

                        Message msg = new Message();
                        msg.obj = "Data Not Submitted, Please try After Sometime.";
                        mHandler.sendMessage(msg);
                        dialog.dismiss();
                        progressDialog.dismiss();
                        //  finish();
                    }
                }

            } catch (Exception e) {

                e.printStackTrace();
                progressDialog.dismiss();
            }

            return obj2;
        }

        @Override
        protected void onPostExecute(String result) {

            // write display tracks logic here
          //  onResume();
            dialog.dismiss();
            progressDialog.dismiss();  // dismiss dialog


        }
    }


}


