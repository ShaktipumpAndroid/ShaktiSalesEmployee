package adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.GoodsCanModel;
import model.GoodsIssModel;
import model.GoodsRecpModel;


public class GoodCancAdapter extends
        RecyclerView.Adapter<GoodCancAdapter.ViewHolder> {

    private List<GoodsCanModel> gdrList;
    private ArrayList<GoodsCanModel> arraylist;

    public GoodCancAdapter(List<GoodsCanModel> gdr) {
        this.gdrList = gdr;
        this.arraylist = new ArrayList<GoodsCanModel>();
        this.arraylist.addAll(gdrList);

    }

    // Create new views
    @Override
    public GoodCancAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        viewHolder.chkSelected.setChecked(gdrList.get(position).isSelected());

        viewHolder.chkSelected.setTag(gdrList.get(position));


        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                GoodsCanModel contact = (GoodsCanModel) cb.getTag();

                contact.setSelected(cb.isChecked());
                gdrList.get(pos).setSelected(cb.isChecked());

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

        }

    }

    // method to access in activity after updating selection
    public List<GoodsCanModel> getgoodstist() {
        return gdrList;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

       gdrList.clear();
        if (charText.length() == 0) {
            gdrList.addAll(arraylist);
        } else {
            for (GoodsCanModel sc : arraylist) {

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