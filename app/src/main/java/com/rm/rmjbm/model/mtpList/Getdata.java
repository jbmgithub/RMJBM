
package com.rm.rmjbm.model.mtpList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Getdata {

    @SerializedName("matnr")
    @Expose
    private String matnr;
    @SerializedName("maktx")
    @Expose
    private String maktx;
    @SerializedName("meins")
    @Expose
    private String meins;
    @SerializedName("packq")
    @Expose
    private Integer packq;
    @SerializedName("charg")
    @Expose
    private String charg;
    @SerializedName("labst")
    @Expose
    private Integer labst;
    @SerializedName("scanq")
    @Expose
    private String scanq;
    @SerializedName("scanp")
    @Expose
    private Integer scanp;

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMeins() {
        return meins;
    }

    public void setMeins(String meins) {
        this.meins = meins;
    }

    public Integer getPackq() {
        return packq;
    }

    public void setPackq(Integer packq) {
        this.packq = packq;
    }

    public String getCharg() {
        return charg;
    }

    public void setCharg(String charg) {
        this.charg = charg;
    }

    public Integer getLabst() {
        return labst;
    }

    public void setLabst(Integer labst) {
        this.labst = labst;
    }

    public String getScanq() {
        return scanq;
    }

    public void setScanq(String scanq) {
        this.scanq = scanq;
    }

    public Integer getScanp() {
        return scanp;
    }

    public void setScanp(Integer scanp) {
        this.scanp = scanp;
    }

}
