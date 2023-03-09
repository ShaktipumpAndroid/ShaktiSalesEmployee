package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoodsRecpModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String docno;
    private String docitm;
    private String docyear;
    private String matnr;
    private String qty;
    private String arktx;
    private String sernr;
    private String sender;
    private String sender_typ;
    private String senderNm;
    private String receiver;
    private String receiver_typ;
    private String receiverNm;

    private boolean isSelected;

    public GoodsRecpModel() {

    }

    public GoodsRecpModel(String docno,
                          String docitm,
                          String docyear,
                          String matnr,
                          String qty,
                          String arktx,
                          String sernr,
                          String sender_typ,
                          String senderNm,
                          String receiver,
                          String receiver_typ,
            String receiverNm) {

       this.docno  = docno;
       this.docitm  = docitm;
       this.docyear  = docyear;
       this.matnr  = matnr;
       this.qty  = qty;
       this.arktx  = arktx;
       this.sernr  = sernr;
       this.sender  = sender;
       this.sender_typ  = sender_typ;
       this.receiver  = receiver;
       this.receiver_typ  = receiver_typ;
       this.senderNm  = senderNm;
       this.receiverNm  = receiverNm;


    }

    public GoodsRecpModel(String docno,
                          String docitm,
                          String docyear,
                          String matnr,
                          String qty,
                          String arktx,
                          String sernr,
                          String sender,
                          String sender_typ,
                          String senderNm,
                          String receiver,
                          String receiver_typ,
                          String receiverNm, boolean isSelected) {

        this.docno  = docno;
        this.docitm  = docitm;
        this.docyear  = docyear;
        this.matnr  = matnr;
        this.qty  = qty;
        this.arktx  = arktx;
        this.sernr  = sernr;
        this.sender  = sender;
        this.sender_typ  = sender_typ;
        this.receiver  = receiver;
        this.receiver_typ  = receiver_typ;
        this.senderNm  = senderNm;
        this.receiverNm  = receiverNm;
        this.isSelected = isSelected;
    }


    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getDocno() {
        return docno;
    }

    public void setDocno(String docno) {
        this.docno = docno;
    }

    public String getDocitm() {
        return docitm;
    }

    public void setDocitm(String docitm) {
        this.docitm = docitm;
    }

    public String getDocyear() {
        return docyear;
    }

    public void setDocyear(String docyear) {
        this.docyear = docyear;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSenderNm() {
        return senderNm;
    }

    public void setSenderNm(String senderNm) {
        this.senderNm = senderNm;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getReceiverNm() {
        return receiverNm;
    }

    public void setReceiverNm(String receiverNm) {
        this.receiverNm = receiverNm;
    }

    public String getSender_typ() {
        return sender_typ;
    }

    public void setSender_typ(String sender_typ) {
        this.sender_typ = sender_typ;
    }

    public String getReceiver_typ() {
        return receiver_typ;
    }

    public void setReceiver_typ(String receiver_typ) {
        this.receiver_typ = receiver_typ;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}