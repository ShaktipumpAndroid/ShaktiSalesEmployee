package customlist;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.shaktipumps.shakti.shaktisalesemployee.R;

import java.util.ArrayList;

import adapter.Controller;
import model.ModelProducts;

/**
 * Created by Administrator on 10/25/2016.
 */
public class SingleProductList extends ArrayAdapter<ModelProducts> {


    final Controller ct;
    Activity context;
    ArrayList<ModelProducts> modelproducts;
    double double_grandtotal = 0;
    double double_total = 0;
    Integer index = 0;

    public SingleProductList(Activity context, ArrayList<ModelProducts> modelproducts) {
        super(context, R.layout.singleproductlist, modelproducts);
        this.context = context;

        this.modelproducts = modelproducts;
        ct = (Controller) context.getApplicationContext();
    }

    public void setList(ArrayList<ModelProducts> modelproducts) {
        this.modelproducts = modelproducts;
    }

    @Override
    public View getView(final int pos, View view, final ViewGroup parent) {

        final int position = pos;

        // Get the data item for this position
        final ModelProducts modelproduct = getItem(pos);

        // Check if an existing view is being reused, otherwise inflate the view

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.singleproductlist, parent, false);
        }


        // Lookup view for data population

        TextView txt_model = (TextView) view.findViewById(R.id.textView_model);
        TextView txt_product = (TextView) view.findViewById(R.id.textView_product);
        TextView txt_discrption = (TextView) view.findViewById(R.id.textView_discription);
        TextView txt_price = (TextView) view.findViewById(R.id.textView_price);
        final TextView txt_total_price = (TextView) view.findViewById(R.id.edittext_total_price);
        final TextView txt_quantity = (TextView) view.findViewById(R.id.EditText_quantity);
        //final TextView txt_grand_total = (TextView) view.findViewById(R.id.textView_gradtotal);


        txt_model.setText(String.valueOf(modelproduct.getModel()));
        txt_quantity.setText(String.valueOf(modelproduct.getQuantity()));
        txt_product.setText(String.valueOf(modelproduct.getProduct()));
        txt_discrption.setText(String.valueOf(modelproduct.getDiscription()));
        txt_price.setText(String.valueOf("Price: " + modelproduct.getPrice()));
        txt_total_price.setText(String.valueOf("Total: " + modelproduct.getTotal_price()));


        ImageButton button_delete = (ImageButton) view.findViewById(R.id.imageButton_delete);
        button_delete.setTag(pos);

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Integer index = (Integer) v.getTag();

                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
                // Setting Dialog Title
                alertDialog.setTitle("Confirmation");
                // Setting Dialog Message
                alertDialog.setMessage("Are you sure you want to delete this item?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        remove(modelproduct);
                        ct.getCart().removeProduct(position);
                        notifyDataSetChanged();
                        OrderConfirmListview.changeGrandTotal("" + getGrandTotal());

                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // Showing Alert Message
                alertDialog.show();
            }
        });


        ImageButton button_plus = (ImageButton) view.findViewById(R.id.imageButton_plus);
        button_plus.setTag(pos);

        button_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = (Integer) v.getTag();

                int int_quantity = Integer.valueOf(ct.getCart().getProducts(position).getQuantity()).intValue();
                Double double_price = Double.valueOf(ct.getCart().getProducts(position).getPrice()).doubleValue();

                if (int_quantity < 999) {
                    int_quantity = int_quantity + 1;
                    Double double_total_price = Double.valueOf(int_quantity * double_price);
                    ct.getCart().getProducts(position).setQuantity("" + int_quantity);
                    ct.getCart().getProducts(position).setTotal_price("" + double_total_price);
                    txt_quantity.setText("" + int_quantity);
                    modelproducts.get(position).setQuantity("" + int_quantity);
                    txt_total_price.setText("Total: " + double_total_price);
                    modelproducts.get(position).setTotal_price("" + double_total_price);
                    notifyDataSetChanged();
                    OrderConfirmListview.changeGrandTotal("" + getGrandTotal());
                }
            }
        });


        ImageButton button_minus = (ImageButton) view.findViewById(R.id.imageButton_minus);
        button_minus.setTag(pos);

        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer index = (Integer) v.getTag();


                int int_quantity = Integer.valueOf(ct.getCart().getProducts(position).getQuantity()).intValue();
                Double double_price = Double.valueOf(ct.getCart().getProducts(position).getPrice()).doubleValue();

                if (int_quantity > 1) {
                    int_quantity = int_quantity - 1;
                    Double double_total_price = Double.valueOf(int_quantity * double_price);
                    ct.getCart().getProducts(position).setQuantity("" + int_quantity);
                    ct.getCart().getProducts(position).setTotal_price("" + double_total_price);
                    txt_quantity.setText("" + int_quantity);
                    modelproducts.get(position).setQuantity("" + int_quantity);
                    txt_total_price.setText("Total: " + double_total_price);
                    modelproducts.get(position).setTotal_price("" + double_total_price);
                    notifyDataSetChanged();
                    OrderConfirmListview.changeGrandTotal("" + getGrandTotal());
                }
            }
        });

        return view;
    }


    public double getGrandTotal() {
        final int cartsize = ct.getCart().getCartsize();

        if (cartsize > 0) {
            double_grandtotal = 0;
            for (int i = 0; i < cartsize; i++) {
                String total_price = ct.getCart().getProducts(i).getTotal_price();
                double_total = Double.valueOf(total_price);
                double_grandtotal = double_grandtotal + double_total;
            }
        } else {
            double_grandtotal = 0;
        }
        return double_grandtotal;
    }


}

