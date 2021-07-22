
package com.rm.rmjbm.model.movementLov;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("werks")
    @Expose
    private Integer werks;
    @SerializedName("bwart")
    @Expose
    private Integer bwart;
    @SerializedName("lgort_f")
    @Expose
    private String lgortF;
    @SerializedName("lgort_t")
    @Expose
    private String lgortT;

    public Integer getWerks() {
        return werks;
    }

    public void setWerks(Integer werks) {
        this.werks = werks;
    }

    public Integer getBwart() {
        return bwart;
    }

    public void setBwart(Integer bwart) {
        this.bwart = bwart;
    }

    public String getLgortF() {
        return lgortF;
    }

    public void setLgortF(String lgortF) {
        this.lgortF = lgortF;
    }

    public String getLgortT() {
        return lgortT;
    }

    public void setLgortT(String lgortT) {
        this.lgortT = lgortT;
    }

}
