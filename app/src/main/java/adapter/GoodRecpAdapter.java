package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activity.CustomUtility;
import activity.GoodsIssue_Activity1;
import activity.GoodsTransSub_Activity;
import model.GoodsRecpModel;
import searchlist.complaint.SearchComplaint;


public class GoodRecpAdapter extends
        RecyclerView.Adapter<GoodRecpAdapter.ViewHolder> {

    private List<GoodsRecpModel> gdrList;
    private ArrayList<GoodsRecpModel> arraylist;
    private String type;
    private Context context;

    public GoodRecpAdapter(List<GoodsRecpModel> gdr, String type, Context context) {
        this.gdrList = gdr;
        this.context = context;
        this.type = type;
        this.arraylist = new ArrayList<GoodsRecpModel>();
        this.arraylist.addAll(gdrList);

    }

    // Create new views
    @Override
    public GoodRecpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_item, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.docnoitmyr_txt.setText(gdrList.get(position).getDocno() +"/"+ gdrList.get(position).getDocitm() +"/"+ gdrList.get(position).getDocyear());
        viewHolder.matnr_txt.setText(gdrList.get(position).getMatnr());
        viewHolder.sernr_txt.setText(gdrList.get(position).getSernr());
        viewHolder.arktx_txt.setText(gdrList.get(position).getArktx());
        viewHolder.qty_txt.setText(gdrList.get(position).getQty());
        viewHolder.sender_txt.setText(gdrList.get(position).getSender() +"/"+ gdrList.get(position).getSenderNm());
        viewHolder.rec_txt.setText(gdrList.get(position).getReceiver() +"/"+ gdrList.get(position).getReceiverNm());

        if(type.equalsIgnoreCase("T"))
        {
            viewHolder.chkSelected.setVisibility(View.GONE);
        }
        else{
            viewHolder.chkSelected.setVisibility(View.VISIBLE);
        }

        viewHolder.chkSelected.setChecked(gdrList.get(position).isSelected());

        viewHolder.chkSelected.setTag(gdrList.get(position));


        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                GoodsRecpModel contact = (GoodsRecpModel) cb.getTag();

                contact.setSelected(cb.isChecked());
                gdrList.get(pos).setSelected(cb.isChecked());

            }
        });

        viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (CustomUtility.isOnline(context)) {

                    String doc_no =  gdrList.get(pos).getDocno();
                    String doc_itm =  gdrList.get(pos).getDocitm();
                    String doc_yr =  gdrList.get(pos).getDocyear();
                    String matnr =  gdrList.get(pos).getMatnr();
                    String matnrnm =  gdrList.get(pos).getArktx();
                    String sernr =  gdrList.get(pos).getSernr();

                    Intent intent = new Intent(context, GoodsTransSub_Activity.class);
                    intent.putExtra("doc_no", doc_no);
                    intent.putExtra("doc_item", doc_itm);
                    intent.putExtra("doc_year", doc_yr);
                    intent.putExtra("matnr", matnr);
                    intent.putExtra("matnrnm", matnrnm);
                    intent.putExtra("sernr", sernr);
                    context.startActivity(intent);
                }
                else {
                    Toast.makeText(context, "No internet connection...., Please try again", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return gdrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView docnoitmyr_txt;
        public TextView matnr_txt;
        public TextView sernr_txt;
        public TextView arktx_txt;
        public TextView qty_txt;
        public TextView sender_txt;
        public TextView rec_txt;

        public CheckBox chkSelected;
        public CardView cardView;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            docnoitmyr_txt = (TextView) itemLayoutView.findViewById(R.id.docnoitmyr_txt);

            matnr_txt = (TextView) itemLayoutView.findViewById(R.id.matnr_txt);
            sernr_txt = (TextView) itemLayoutView.findViewById(R.id.sernr_txt);
            qty_txt = (TextView) itemLayoutView.findViewById(R.id.qty_txt);
            arktx_txt = (TextView) itemLayoutView.findViewById(R.id.arktx_txt);
            sender_txt = (TextView) itemLayoutView.findViewById(R.id.sender_txt);
            rec_txt = (TextView) itemLayoutView.findViewById(R.id.rec_txt);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
            cardView = (CardView) itemLayoutView.findViewById(R.id.card_view);

        }

    }

    // method to access in activity after updating selection
    public List<GoodsRecpModel> getGoodsist() {
        return gdrList;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

       gdrList.clear();
        if (charText.length() == 0) {
            gdrList.addAll(arraylist);
        } else {
            for (GoodsRecpModel sc : arraylist) {

                if (sc.getMatnr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getReceiver().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getSender().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getSenderNm().toLowerCase(Locale.getDefault()).contains(charText)||
                        sc.getReceiverNm().toLowerCase(Locale.getDefault()).contains(charText)||
                        sc.getArktx().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getSernr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getDocno().toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                    gdrList.add(sc);
                }


            }
        }
        notifyDataSetChanged();
    }

}