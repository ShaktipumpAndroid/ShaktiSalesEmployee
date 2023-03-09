package model;


import java.io.Serializable;

public class GoodsIssModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String matnr;
    private String qty;
    private String maktx;
    private String sernr;
    private String sernr1;
    private String qty1;

    private boolean isSelected;

    public GoodsIssModel() {

    }

    public GoodsIssModel(String matnr,
                          String qty,
                          String maktx,
                          String sernr,
                          String sernr1,
                          String qty1) {


        this.matnr  = matnr;
        this.qty  = qty;
        this.maktx  = maktx;
        this.sernr  = sernr;
        this.sernr1  = sernr1;
        this.qty1  = qty1;


    }

    public GoodsIssModel(String matnr,
                          String qty,
                          String maktx,
                          String sernr,
                          String sernr1,String qty1, boolean isSelected) {


        this.matnr  = matnr;
        this.qty  = qty;
        this.maktx  = maktx;
        this.sernr  = sernr;
        this.sernr1  = sernr1;
        this.qty1  = qty1;
        this.isSelected = isSelected;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getSernr() {
        return sernr;
    }

    public void setSernr(String sernr) {
        this.sernr = sernr;
    }

    public String getSernr1() {
        return sernr1;
    }

    public void setSernr1(String sernr1) {
        this.sernr1 = sernr1;
    }

    public String getQty1() {
        return qty1;
    }

    public void setQty1(String qty1) {
        this.qty1 = qty1;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}