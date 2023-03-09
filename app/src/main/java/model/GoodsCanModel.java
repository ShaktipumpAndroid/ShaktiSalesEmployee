package model;


import java.io.Serializable;

public class GoodsCanModel implements Serializable {

    private static final long serialVersionUID = 1L;
    private String docno;
    private String docitm;
    private String docyear;
    private String matnr;
    private String qty;
    private String arktx;
    private String sernr;
    private String sender;
    private String senderNm;
    private String receiver;
    private String receiverNm;

    private boolean isSelected;

    public GoodsCanModel() {

    }

    public GoodsCanModel(String docno,
                         String docitm,
                         String docyear,
                         String matnr,
                         String qty,
                         String arktx,
                         String sernr,
                         String sender,
                         String senderNm,
                         String receiver,
                         String receiverNm) {

        this.docno  = docno;
        this.docitm  = docitm;
        this.docyear  = docyear;
        this.matnr  = matnr;
        this.qty  = qty;
        this.arktx  = arktx;
        this.sernr  = sernr;
        this.sender  = sender;
        this.receiver  = receiver;
        this.senderNm  = senderNm;
        this.receiverNm  = receiverNm;


    }

    public GoodsCanModel(String docno,
                         String docitm,
                         String docyear,
                         String matnr,
                         String qty,
                         String arktx,
                         String sernr,
                         String sender,
                         String senderNm,
                         String receiver,
                         String receiverNm, boolean isSelected) {

        this.docno  = docno;
        this.docitm  = docitm;
        this.docyear  = docyear;
        this.matnr  = matnr;
        this.qty  = qty;
        this.arktx  = arktx;
        this.sernr  = sernr;
        this.sender  = sender;
        this.receiver  = receiver;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}