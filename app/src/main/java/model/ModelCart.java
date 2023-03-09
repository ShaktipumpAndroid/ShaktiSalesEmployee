package model;

import java.util.ArrayList;

/**
 * Created by Administrator on 10/17/2016.
 */
public class ModelCart {
    private ArrayList<ModelProducts> cartItems = new ArrayList<ModelProducts>();

    public ModelProducts getProducts(int position) {
        return cartItems.get(position);
    }

    public void setProducts(ModelProducts Products) {
        cartItems.add(Products);
    }

    public int getCartsize() {

        return cartItems.size();
    }

    public void removeProduct(int position) {
        cartItems.remove(position);

    }


    public void clearCart() {
        cartItems.clear();

    }

    public boolean CheckProductInCart(ModelProducts aproduct) {
        return cartItems.contains(aproduct);
    }

}