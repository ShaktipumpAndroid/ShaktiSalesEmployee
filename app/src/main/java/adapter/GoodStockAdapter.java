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

import model.GoodsRecpModel;
import model.GoodsStockrRepModel;


public class GoodStockAdapter extends
        RecyclerView.Adapter<GoodStockAdapter.ViewHolder> {

    private List<GoodsStockrRepModel> gdrList;
    private ArrayList<GoodsStockrRepModel> arraylist;

    public GoodStockAdapter(List<GoodsStockrRepModel> gdr) {
        this.gdrList = gdr;
        this.arraylist = new ArrayList<GoodsStockrRepModel>();
        this.arraylist.addAll(gdrList);

    }

    // Create new views
    @Override
    public GoodStockAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_item1, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        final int pos = position;


        viewHolder.matnr_txt.setText(gdrList.get(position).getMatnr());
        viewHolder.sernr_txt.setText(gdrList.get(position).getSernr());
        viewHolder.arktx_txt.setText(gdrList.get(position).getArktx());
        viewHolder.qty_txt.setText(gdrList.get(position).getQty());

        viewHolder.chkSelected.setVisibility(View.GONE);

    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return gdrList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public TextView matnr_txt;
        public TextView sernr_txt;
        public TextView arktx_txt;
        public TextView qty_txt;
        public CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);



            matnr_txt = (TextView) itemLayoutView.findViewById(R.id.matnr_txt);
            sernr_txt = (TextView) itemLayoutView.findViewById(R.id.sernr_txt);
            qty_txt = (TextView) itemLayoutView.findViewById(R.id.qty_txt);
            arktx_txt = (TextView) itemLayoutView.findViewById(R.id.arktx_txt);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);

        }

    }

    // method to access in activity after updating selection
    public List<GoodsStockrRepModel> getGoodsist() {
        return gdrList;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

       gdrList.clear();
        if (charText.length() == 0) {
            gdrList.addAll(arraylist);
        } else {
            for (GoodsStockrRepModel sc : arraylist) {

                if (sc.getMatnr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getArktx().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getSernr().toLowerCase(Locale.getDefault()).contains(charText)

                ) {
                    gdrList.add(sc);
                }


            }
        }
        notifyDataSetChanged();
    }

}