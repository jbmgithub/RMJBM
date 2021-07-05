
package com.rm.rmjbm.model.scanPhyInventoryData;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MtRmBcPhyInvScanRec {

    @SerializedName("BATCH")
    @Expose
    private String batch;
    @SerializedName("COUNT")
    @Expose
    private Integer count;
    @SerializedName("MAKTX")
    @Expose
    private String maktx;
    @SerializedName("MATNR")
    @Expose
    private String matnr;
    @SerializedName("MESSAGE")
    @Expose
    private String message;
    @SerializedName("QTY")
    @Expose
    private String qty;
    @SerializedName("STATUS")
    @Expose
    private String status;

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMaktx() {
        return maktx;
    }

    public void setMaktx(String maktx) {
        this.maktx = maktx;
    }

    public String getMatnr() {
        return matnr;
    }

    public void setMatnr(String matnr) {
        this.matnr = matnr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
