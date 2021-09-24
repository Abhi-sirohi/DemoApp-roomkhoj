package me.abhinaysirohi;

public class Messdemo1 {
    String tname, tdesc;
    long tphone;
    int tprice;
    String timg;
    String tmenu;
    Float tr;
    int trsr;



    Messdemo1() {

    }



    public Messdemo1(String tname, String tdesc, String timg, long tphone, int tprice, String tmenu) {
        this.tname = tname;
        this.tdesc = tdesc;
        this.tphone = tphone;
        this.tprice = tprice;
        this.timg = timg;
    }
    public Float getTr() {
        return tr;
    }

    public void setTr(Float tr) {
        this.tr = tr;
    }

    public int getTrsr() {
        return trsr;
    }

    public void setTrsr(int trsr) {
        this.trsr = trsr;
    }

    public String getTname() {
        return tname;
    }

    public void setTname(String tname) {
        this.tname = tname;
    }

    public String getTdesc() {
        return tdesc;
    }

    public void setTdesc(String tdesc) {
        this.tdesc = tdesc;
    }


    public long getTphone() {
        return tphone;
    }

    public void setTphone(long tphone) {
        this.tphone = tphone;
    }

    public int getTprice() {
        return tprice;
    }

    public void setTprice(int tprice) {
        this.tprice = tprice;
    }

    public String getTimg() {
        return timg;
    }

    public void setTimg(String timg) {
        this.timg = timg;
    }
    public String getTmenu() {
        return tmenu;
    }

    public void setTmenu(String tmenu) {
        this.tmenu = tmenu;
    }
}
