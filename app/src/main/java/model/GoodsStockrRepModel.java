package model;


import java.io.Serializable;

public class GoodsStockrRepModel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String matnr;
    private String qty;
    private String arktx;
    private String sernr;


    public GoodsStockrRepModel() {

    }

    public GoodsStockrRepModel(
                               String matnr,
                               String qty,
                               String arktx,
                               String sernr
                              ) {


        this.matnr  = matnr;
        this.qty  = qty;
        this.arktx  = arktx;
        this.sernr  = sernr;



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

    public String getArktx() {
        return arktx;
    }

    public void setArktx(String arktx) {
        this.arktx = arktx;
    }

    public String getSernr() {
        return sernr;
    }

    public void setSernr(String sernr) {
        this.sernr = sernr;
    }

}