
package com.rm.rmjbm.model.mtpList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MTMTPGetlistRec {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("getdata")
    @Expose
    private Object getdata = null;

    public String getSuccess() {
        return success;
    }

    public Object getGetdata() {
        return getdata;
    }

    public void setGetdata(Object getdata) {
        this.getdata = getdata;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
