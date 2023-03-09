package searchlist.complaint;

/**
 * Created by shakti on 04-Oct-18.
 */
public class Complaint_Pdf_Name {

    String cmpno = "", pernr = "",
            name1 = "",
            name2 = "";

    public Complaint_Pdf_Name() {

    }


    public Complaint_Pdf_Name(String pernr_txt, String cmp_no_txt,
                              String name1_txt,
                              String name2_txt) {

        pernr = pernr_txt;
        cmpno = cmp_no_txt;
        name1 = name1_txt;
        name2 = name2_txt;

    }


    public String getName1() {
        return name1;
    }

    public void setName1(String name1) {
        this.name1 = name1;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public String getCmpno() {
        return cmpno;
    }

    public void setCmpno(String cmpno) {
        this.cmpno = cmpno;
    }

    public String getPernr() {
        return pernr;
    }

    public void setPernr(String pernr) {
        this.pernr = pernr;
    }
}
