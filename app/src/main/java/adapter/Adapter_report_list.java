package adapter;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import activity.DisplayReviewComplaintDetailActivity;
import webservice.NotifyInterface;


public class Adapter_report_list extends RecyclerView.Adapter<adapter.Adapter_report_list.HomeCategoryViewHolder> {

    private ArrayList<JSONArray> jsonArray;

    private Context context;
    private NotifyInterface notifyInterface;


    public Adapter_report_list(Context context, ArrayList<JSONArray> jsonArray, NotifyInterface notifyInterface) {
        this.context = context;
        this.jsonArray = jsonArray;
        this.notifyInterface = notifyInterface;


    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adpter_report_list, parent, false);

        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {


        try {


            if (!TextUtils.isEmpty(jsonArray.get(position).getJSONObject(position).optString("bukrs"))) {

                holder.plant_code.setText(jsonArray.get(position).getJSONObject(position).optString("bukrs"));

            }
            if (!TextUtils.isEmpty(jsonArray.get(position).getJSONObject(position).optString("cmpdt"))) {

                holder.cmp_date.setText(jsonArray.get(position).getJSONObject(position).optString("cmpdt"));

            }
            if (!TextUtils.isEmpty(jsonArray.get(position).getJSONObject(position).optString("cmpno"))) {

                holder.cmp_num.setText(jsonArray.get(position).getJSONObject(position).optString("cmpno"));

            }
            if (!TextUtils.isEmpty(jsonArray.get(position).getJSONObject(position).optString("matkl"))) {

                holder.mat_cd.setText(jsonArray.get(position).getJSONObject(position).optString("matkl"));

            }
            if (!TextUtils.isEmpty(jsonArray.get(position).getJSONObject(position).optString("maktx"))) {

                holder.mat_desc.setText(jsonArray.get(position).getJSONObject(position).optString("maktx"));

            }

            ((HomeCategoryViewHolder) holder).cardView.setTag(position);

            ((HomeCategoryViewHolder) holder).cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = (Integer) view.getTag();


                    try {

                        String bukrs = jsonArray.get(pos).getJSONObject(pos).optString("bukrs");
                        String cmpdt = jsonArray.get(pos).getJSONObject(pos).getString("cmpdt");
                        String cstname = jsonArray.get(pos).getJSONObject(pos).getString("cstname");
                        String kunnr = jsonArray.get(pos).getJSONObject(pos).getString("kunnr");
                        String matkl = jsonArray.get(pos).getJSONObject(pos).getString("matkl");
                        String pwerks = jsonArray.get(pos).getJSONObject(pos).getString("pwerks");
                        String cmpno = jsonArray.get(pos).getJSONObject(pos).getString("cmpno");
                        String posnr = jsonArray.get(pos).getJSONObject(pos).getString("posnr");
                        String reason = jsonArray.get(pos).getJSONObject(pos).getString("reason");
                        String closer_reason = jsonArray.get(pos).getJSONObject(pos).getString("closer_reason");
                        String matnr = jsonArray.get(pos).getJSONObject(pos).getString("matnr");
                        String maktx = jsonArray.get(pos).getJSONObject(pos).getString("maktx");
                        String cmldt = jsonArray.get(pos).getJSONObject(pos).getString("cmldt");
                        String pdate = jsonArray.get(pos).getJSONObject(pos).getString("pdate");
                        String sernr = jsonArray.get(pos).getJSONObject(pos).getString("sernr");
                        String l_action = jsonArray.get(pos).getJSONObject(pos).getString("l_action");


                        Intent i = new Intent(context, DisplayReviewComplaintDetailActivity.class);
                        // sending data to new activity
                        Bundle extras = new Bundle();
                        extras.putString("company_code", bukrs);
                        extras.putString("comp_date", cmpdt);
                        extras.putString("dealer_name", cstname);
                        extras.putString("dealer_no", kunnr);
                        extras.putString("mat_grp", matkl);
                        extras.putString("prod_plant", pwerks);
                        extras.putString("comp_no", cmpno);
                        extras.putString("item_no", posnr);
                        extras.putString("reason", reason);
                        extras.putString("close_reason", closer_reason);
                        extras.putString("mat_no", matnr);
                        extras.putString("mat_des", maktx);
                        extras.putString("comp_cls_date", cmldt);
                        extras.putString("prod_date", pdate);
                        extras.putString("sear_no", sernr);
                        extras.putString("last_action", l_action);
                        i.putExtras(extras);
                        context.startActivity(i);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return jsonArray.size();
    }


    public class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView plant_code, cmp_date, cmp_num, mat_cd, mat_desc;
        CardView cardView;

        public HomeCategoryViewHolder(View itemView) {
            super(itemView);

            cmp_date = (TextView) itemView.findViewById(R.id.cmp_date);
            plant_code = (TextView) itemView.findViewById(R.id.plant_code);
            cmp_num = (TextView) itemView.findViewById(R.id.cmp_num);
            mat_cd = (TextView) itemView.findViewById(R.id.mat_cd);
            mat_desc = (TextView) itemView.findViewById(R.id.mat_desc);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }
    }


}