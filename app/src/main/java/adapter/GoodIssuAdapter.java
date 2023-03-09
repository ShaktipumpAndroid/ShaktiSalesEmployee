package adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import model.GoodsIssModel;
import model.GoodsRecpModel;


public class GoodIssuAdapter extends
        RecyclerView.Adapter<GoodIssuAdapter.ViewHolder> {

    private List<GoodsIssModel> gdrList;
    private ArrayList<GoodsIssModel> arraylist;
    private Context context;
    private String cmp_no;

    public GoodIssuAdapter(List<GoodsIssModel> gdr,Context context, String cmp_no) {
        this.context = context;
        this.cmp_no = cmp_no;
        this.gdrList = gdr;
        this.arraylist = new ArrayList<GoodsIssModel>();
        this.arraylist.addAll(gdrList);

    }

    // Create new views
    @Override
    public GoodIssuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.recycler_item1, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        final int pos = position;

        viewHolder.matnr_txt.setText(gdrList.get(position).getMatnr());
        viewHolder.sernr_txt.setText(gdrList.get(position).getSernr());
        viewHolder.maktx_txt.setText(gdrList.get(position).getMaktx());
        viewHolder.qty_txt.setText(gdrList.get(position).getQty());

        if(!TextUtils.isEmpty(cmp_no))
        {
            if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                viewHolder.lin7.setVisibility(View.GONE);
                viewHolder.lin8.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.lin7.setVisibility(View.VISIBLE);
                viewHolder.lin8.setVisibility(View.GONE);
            }

        }
        else {
            if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                viewHolder.lin7.setVisibility(View.GONE);
                viewHolder.lin8.setVisibility(View.VISIBLE);
            }
            else{
                viewHolder.lin7.setVisibility(View.GONE);
                viewHolder.lin8.setVisibility(View.GONE);
            }
        }


        viewHolder.out_sernr_txt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                if(s.length() != 0){
                    GoodsIssModel contact = (GoodsIssModel)  viewHolder.chkSelected.getTag();
                    contact.setSelected(false);
                    viewHolder.chkSelected.setChecked(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    GoodsIssModel contact = (GoodsIssModel)  viewHolder.chkSelected.getTag();
                    contact.setSelected(false);
                    viewHolder.chkSelected.setChecked(false);
                }
            }
        });

        viewHolder.qty1_txt.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                if(s.length() != 0){
                    GoodsIssModel contact = (GoodsIssModel)  viewHolder.chkSelected.getTag();
                    contact.setSelected(false);
                    viewHolder.chkSelected.setChecked(false);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0) {
                    GoodsIssModel contact = (GoodsIssModel)  viewHolder.chkSelected.getTag();
                    contact.setSelected(false);
                    viewHolder.chkSelected.setChecked(false);
                }

            }
        });



        viewHolder.chkSelected.setChecked(gdrList.get(position).isSelected());

        viewHolder.chkSelected.setTag(gdrList.get(position));

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!TextUtils.isEmpty(viewHolder.qty1_txt.getText().toString()) || !TextUtils.isEmpty(viewHolder.out_sernr_txt.getText().toString())) {
                    CheckBox cb = (CheckBox) v;
                    GoodsIssModel contact = (GoodsIssModel) cb.getTag();
                    contact.setSelected(cb.isChecked());
                    if(!cb.isChecked())
                    {
                        viewHolder.qty1_txt.setText("");
                        viewHolder.out_sernr_txt.setText("");
                    }
                    gdrList.get(pos).setSelected(cb.isChecked());
                    if (gdrList.get(pos).isSelected()) {

                        if(!TextUtils.isEmpty(cmp_no)) {
                            if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                                float qty = Float.parseFloat(gdrList.get(pos).getQty());
                                float qty1 = Float.parseFloat(viewHolder.qty1_txt.getText().toString());
                                if(qty1 > 0 )
                                {
                                    if(qty1 <= qty ) {

                                        if (!TextUtils.isEmpty(viewHolder.qty1_txt.getText().toString())) {
                                            contact.setQty1(viewHolder.qty1_txt.getText().toString());
                                        }

                                    }
                                    else{
                                        viewHolder.chkSelected.setChecked(false);
                                        Toast.makeText(context, "Please enter quantity less then equal to available quantity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    viewHolder.chkSelected.setChecked(false);
                                    Toast.makeText(context, "Please enter quantity greater then zero", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                if (!TextUtils.isEmpty(viewHolder.out_sernr_txt.getText().toString())) {
                                    contact.setSernr1(viewHolder.out_sernr_txt.getText().toString());
                                }
                            }
                        }
                        else{
                            if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                                float qty = Float.parseFloat(gdrList.get(pos).getQty());
                                float qty1 = Float.parseFloat(viewHolder.qty1_txt.getText().toString());
                                if(qty1 > 0 )
                                {
                                    if(qty1 <= qty ) {

                                        if (!TextUtils.isEmpty(viewHolder.qty1_txt.getText().toString())) {
                                            contact.setQty1(viewHolder.qty1_txt.getText().toString());
                                        }

                                    }
                                    else{
                                        viewHolder.chkSelected.setChecked(false);
                                        Toast.makeText(context, "Please enter quantity less then equal to available quantity", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else{
                                    viewHolder.chkSelected.setChecked(false);
                                    Toast.makeText(context, "Please enter quantity greater then zero", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                }
                else if(TextUtils.isEmpty(viewHolder.out_sernr_txt.getText().toString()) && TextUtils.isEmpty(cmp_no)) {

                    CheckBox cb = (CheckBox) v;
                    GoodsIssModel contact = (GoodsIssModel) cb.getTag();
                    contact.setSelected(cb.isChecked());
                    if(!cb.isChecked())
                    {
                        viewHolder.qty1_txt.setText("");
                        viewHolder.out_sernr_txt.setText("");
                    }
                    gdrList.get(pos).setSelected(cb.isChecked());
                    if (gdrList.get(pos).isSelected()) {
                    if (TextUtils.isEmpty(cmp_no)) {
                        if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                            if(!TextUtils.isEmpty(viewHolder.qty1_txt.getText().toString()) ) {
                                float qty = Float.parseFloat(gdrList.get(pos).getQty());
                                float qty1 = Float.parseFloat(viewHolder.qty1_txt.getText().toString());
                                if (qty1 > 0) {
                                    if (qty1 <= qty) {

                                        if (!TextUtils.isEmpty(viewHolder.qty1_txt.getText().toString())) {
                                            contact.setQty1(viewHolder.qty1_txt.getText().toString());
                                        }

                                    } else {
                                        viewHolder.chkSelected.setChecked(false);
                                        Toast.makeText(context, "Please enter quantity less then equal to available quantity", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    viewHolder.chkSelected.setChecked(false);
                                    Toast.makeText(context, "Please enter quantity greater then zero", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                viewHolder.chkSelected.setChecked(false);
                                Toast.makeText(context, "Please enter quantity first, then select checkbox", Toast.LENGTH_SHORT).show();
                            }
                        }


                    }
                }

                }
                else{

                    if(!TextUtils.isEmpty(cmp_no)) {
                        viewHolder.chkSelected.setChecked(false);
                        if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                            Toast.makeText(context, "Please enter quantity first, then select checkbox", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Please enter serial no. first, then select checkbox", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        if (TextUtils.isEmpty(gdrList.get(pos).getSernr())) {
                            viewHolder.chkSelected.setChecked(false);
                            Toast.makeText(context, "Please enter quantity first, then select checkbox", Toast.LENGTH_SHORT).show();
                        }
                    }
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

        public TextView matnr_txt;
        public TextView sernr_txt;
        public TextView maktx_txt;
        public TextView qty_txt;
        public TextView out_sernr;
        public EditText qty1_txt;
        public EditText out_sernr_txt;
        public TextView sernr_txt1;
        public LinearLayout lin7;
        public LinearLayout lin8;


        public CheckBox chkSelected;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            matnr_txt = (TextView) itemLayoutView.findViewById(R.id.matnr_txt);
            sernr_txt = (TextView) itemLayoutView.findViewById(R.id.sernr_txt);
            qty_txt = (TextView) itemLayoutView.findViewById(R.id.qty_txt);
            maktx_txt = (TextView) itemLayoutView.findViewById(R.id.arktx_txt);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);
            qty1_txt = (EditText) itemLayoutView.findViewById(R.id.qty1_txt);
            out_sernr = (TextView) itemLayoutView.findViewById(R.id.out_sernr);
            out_sernr_txt = (EditText) itemLayoutView.findViewById(R.id.out_sernr_txt);
            lin7 = (LinearLayout) itemLayoutView.findViewById(R.id.lin7);
            lin8 = (LinearLayout) itemLayoutView.findViewById(R.id.lin8);

        }

    }

    // method to access in activity after updating selection
    public List<GoodsIssModel> getgoodstist() {
        return gdrList;
    }


    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());

       gdrList.clear();
        if (charText.length() == 0) {
            gdrList.addAll(arraylist);
        } else {
            for (GoodsIssModel sc : arraylist) {

                if (sc.getMatnr().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getMaktx().toLowerCase(Locale.getDefault()).contains(charText) ||
                        sc.getSernr().toLowerCase(Locale.getDefault()).contains(charText)
                ) {
                    gdrList.add(sc);
                }


            }
        }
        notifyDataSetChanged();
    }

}