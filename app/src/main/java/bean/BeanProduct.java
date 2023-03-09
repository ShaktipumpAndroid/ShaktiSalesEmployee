package bean;

/**
 * Created by Administrator on 10/17/2016.
 */
public class BeanProduct {

    String matnr, kunnr, vkorg, vtweg, extwg, maktx, kbetr, konwa, mtart;

    public BeanProduct() {

    }

    public BeanProduct(String matnr, String kunnr, String vkorg, String vtweg, String extwg, String maktx, String kbetr, String konwa, String mtart) {
        this.matnr = matnr;
        this.kunnr = kunnr;
        this.vkorg = vkorg;
        this.vtweg = vtweg;
        this.extwg = extwg;
        this.maktx = maktx;
        this.kbetr = kbetr;
        this.konwa = konwa;
        this.mtart = mtart;

    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getVkorg() {
        return vkorg;
    }

    public void setVkorg(String vkorg) {
        this.vkorg = vkorg;
    }

    public String getVtweg() {
        return vtweg;
    }

    public void setVtweg(String vtweg) {
        this.vtweg = vtweg;
    }

    public String getExtwg() {
        return extwg;
    }

    public void setExtwg(String extwg) {
        this.extwg = extwg;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getKbetr() {
        return kbetr;
    }

    public void setKbetr(String kbetr) {
        this.kbetr = kbetr;
    }

    public String getMtart() {
        return mtart;
    }

    public void setMtart(String mtart) {
        this.mtart = mtart;
    }


    public String getKunnr() {
        return kunnr;
    }

    public void setKunnr(String kunnr) {
        this.kunnr = kunnr;
    }

    public String getKonwa() {
        return konwa;
    }

    public void setKonwa(String konwa) {
        this.konwa = konwa;
    }
}
