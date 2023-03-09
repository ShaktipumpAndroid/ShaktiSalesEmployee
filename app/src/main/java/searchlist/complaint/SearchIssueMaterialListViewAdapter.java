package searchlist.complaint;


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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.complaint.IssueMaterialComplaintActivity;


public class SearchIssueMaterialListViewAdapter extends RecyclerView.Adapter<SearchIssueMaterialListViewAdapter.HomeCategoryViewHolder> {
    // Declare Variables
    Context mContext;
    String cmpno;
    LayoutInflater inflater;
    private List<SearchComplaint1> complaintSearchlist = null;
    private ArrayList<SearchComplaint1> arraylist;

    public SearchIssueMaterialListViewAdapter(Context context, List<SearchComplaint1> complaintSearchlist,String cmpno) {
        mContext = context;
        this.cmpno = cmpno;
        this.complaintSearchlist = complaintSearchlist;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchComplaint1>();
        this.arraylist.addAll(complaintSearchlist);

    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @NonNull
    @Override
    public HomeCategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.search_issuemateriallistview_complaint, parent, false);
        return new HomeCategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final HomeCategoryViewHolder holder, final int position) {

        try {

            holder.cmpno.setText("Complain No. " + cmpno);
            holder.customer_code.setText(complaintSearchlist.get(position).getCustomer_code());
            holder.customer_name.setText(complaintSearchlist.get(position).getCustomer_name());
            holder.matnr_no.setText(complaintSearchlist.get(position).getMatnr());
            holder.matnr_name.setText(complaintSearchlist.get(position).getMaterial_name());
            holder.move_nm.setText(complaintSearchlist.get(position).getBwart());
            holder.qty.setText(complaintSearchlist.get(position).getQuantity());
            holder.msblqty.setText(complaintSearchlist.get(position).getMslbqty());
            holder.freshqty.setText(complaintSearchlist.get(position).getFreshqty());


            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getCustomer_code())) {

                holder.customer_code.setText(complaintSearchlist.get(position).getCustomer_code());

            }

            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getCustomer_name())) {

                holder.customer_name.setText(complaintSearchlist.get(position).getCustomer_name());

            }

            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getMatnr())) {

                holder.matnr_no.setText(complaintSearchlist.get(position).getMatnr());

            }
            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getMaterial_name())) {

                holder.matnr_name.setText(complaintSearchlist.get(position).getMaterial_name());

            }
            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getBwart())) {

                holder.move_nm.setText(complaintSearchlist.get(position).getBwart());

            }
            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getQuantity())) {

                holder.qty.setText(complaintSearchlist.get(position).getQuantity());

            }
            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getMslbqty())) {

                holder.msblqty.setText(complaintSearchlist.get(position).getMslbqty());

            }
            if (!TextUtils.isEmpty(complaintSearchlist.get(position).getFreshqty())) {

                holder.freshqty.setText(complaintSearchlist.get(position).getFreshqty());

            }


            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent in = new Intent(mContext, IssueMaterialComplaintActivity.class);

                    Bundle extras = new Bundle();
                    extras.putString("cmpno", cmpno);
                    extras.putString("cust_code", complaintSearchlist.get(position).getCustomer_code());
                    extras.putString("cust_name",complaintSearchlist.get(position).getCustomer_name());
                    extras.putString("matnr",complaintSearchlist.get(position).getMatnr());
                    extras.putString("matnr_name",complaintSearchlist.get(position).getMaterial_name());
                    extras.putString("vend_code",complaintSearchlist.get(position).getLifnr());
                    extras.putString("vend_name",complaintSearchlist.get(position).getName1());
                    extras.putString("move",complaintSearchlist.get(position).getBwart());
                    extras.putString("qty", complaintSearchlist.get(position).getQuantity());
                    extras.putString("mslbqty",complaintSearchlist.get(position).getMslbqty());
                    extras.putString("freshqty", complaintSearchlist.get(position).getFreshqty());

                    in.putExtras(extras);
                    mContext.startActivity(in);


                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return complaintSearchlist.size();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

        complaintSearchlist.clear();
        if (charText.length() == 0) {
            complaintSearchlist.addAll(arraylist);
        } else {
            for (SearchComplaint1 sc : arraylist) {

                if (sc.getMatnr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getCustomer_code().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getBwart().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getLifnr().toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                    complaintSearchlist.add(sc);
                }


            }
        }
        notifyDataSetChanged();


    }

    class HomeCategoryViewHolder extends RecyclerView.ViewHolder {

        TextView cmpno,customer_code, customer_name,matnr_no,matnr_name,move_nm,qty,msblqty,freshqty;

        CardView cardView;

        HomeCategoryViewHolder(View itemView) {
            super(itemView);

            cmpno = (TextView) itemView.findViewById(R.id.cmpno);
            customer_code = (TextView) itemView.findViewById(R.id.customer_no);
            customer_name = (TextView) itemView.findViewById(R.id.customer_name);
            matnr_no = (TextView) itemView.findViewById(R.id.matnr_no);
            matnr_name = (TextView) itemView.findViewById(R.id.matnr_name);
            move_nm = (TextView) itemView.findViewById(R.id.move_nm);
            qty = (TextView) itemView.findViewById(R.id.qty);
            msblqty = (TextView) itemView.findViewById(R.id.mslbqty);
            freshqty = (TextView) itemView.findViewById(R.id.freshqty);

            cardView = (CardView) itemView.findViewById(R.id.card_view);


        }
    }
}